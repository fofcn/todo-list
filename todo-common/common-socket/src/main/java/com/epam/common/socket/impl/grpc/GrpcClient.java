package com.epam.common.socket.impl.grpc;

import com.epam.common.socket.*;
import com.epam.common.socket.exception.SocketConnectException;
import com.epam.common.socket.exception.SocketException;
import com.epam.common.socket.exception.SocketTimeoutException;
import com.epam.common.socket.util.DirectExecutor;
import com.google.protobuf.Message;
import io.grpc.*;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GrpcClient implements SocketClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcClient.class);

    private static final int RESET_CONN_THRESHOLD = 2;

    private final Map<Endpoint, ManagedChannel> managedChannelPool   = new ConcurrentHashMap<>();

    private final Map<Endpoint, AtomicInteger>  transientFailures    = new ConcurrentHashMap<>();

    private final Map<String, Message> parserClasses;

    private final Map<String, Message> responseMarshallers;

    public GrpcClient(Map<String, Message> parserClasses, Map<String, Message> responseMarshallers) {
        this.parserClasses = parserClasses;
        this.responseMarshallers = responseMarshallers;
    }

    @Override
    public boolean init(SocketConfig config) {
        return true;
    }

    @Override
    public void start() {
        // do nothing
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public void shutdown() {
        closeAllChannels();
        this.transientFailures.clear();
    }

    @Override
    public Object sendSync(Endpoint endpoint, Object request, SendContext sendContext, long timeoutMillis) throws SocketException {
        final CompletableFuture<Object> future = new CompletableFuture<>();

        // just call asynchronous send method and passing a future parameter to callback
        sendAsync(endpoint, request, (result, err) -> {
            if (err == null) {
                future.complete(result);
            } else {
                future.completeExceptionally(err);
            }
        }, sendContext, timeoutMillis);

        try {
            // waiting for response with given timeout milliseconds
            return future.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (final TimeoutException e) {
            future.cancel(true);
            throw new SocketTimeoutException(e);
        } catch (final Throwable t) {
            future.cancel(true);
            throw new SocketException(t);
        }
    }

    @Override
    public void sendAsync(Endpoint endpoint, Object request, SendCallback callback, SendContext sendContext, long timeoutMillis) {

        // executor for error handling
        final Executor executor = callback.executor() != null ? callback.executor() : DirectExecutor.INSTANCE;

        // get or create channel by endpoint
        final Channel ch = getCheckedChannel(endpoint);
        if (ch == null) {
            executor.execute(() -> callback.complete(null, new SocketConnectException("Fail to connect: " + endpoint)));
            return;
        }

        final MethodDescriptor<Message, Message> method = getCallMethod(request);
        final CallOptions callOpts = CallOptions.DEFAULT.withDeadlineAfter(timeoutMillis, TimeUnit.MILLISECONDS);

        ClientCalls.asyncUnaryCall(ch.newCall(method, callOpts), (Message) request, new StreamObserver<Message>() {

            @Override
            public void onNext(final Message value) {
                executor.execute(() -> callback.complete(value, null));
            }

            @Override
            public void onError(final Throwable throwable) {
                executor.execute(() -> callback.complete(null, throwable));
            }

            @Override
            public void onCompleted() {
                // NO-OP
            }
        });
    }

    private MethodDescriptor<Message, Message> getCallMethod(final Object request) {
        final String interest = request.getClass().getName();
        final Message reqIns = this.parserClasses.get(interest);
        return MethodDescriptor //
                .<Message, Message> newBuilder() //
                .setType(MethodDescriptor.MethodType.UNARY) //
                .setFullMethodName(MethodDescriptor.generateFullMethodName(interest, "_call")) //
                .setRequestMarshaller(ProtoUtils.marshaller(reqIns)) //
                .setResponseMarshaller(
                        ProtoUtils.marshaller(this.responseMarshallers.get(interest))) //
                .build();
    }

    private ManagedChannel getCheckedChannel(final Endpoint endpoint) {
        final ManagedChannel ch = getChannel(endpoint, true);

        if (checkConnectivity(endpoint, ch)) {
            return ch;
        }

        return null;
    }

    private ManagedChannel getChannel(final Endpoint endpoint, final boolean createIfAbsent) {
        if (createIfAbsent) {
            return this.managedChannelPool.computeIfAbsent(endpoint, this::newChannel);
        } else {
            return this.managedChannelPool.get(endpoint);
        }
    }

    /**
     * Create a channel and connect to server
     * @param endpoint host/endpoint
     * @return server channel
     */
    private ManagedChannel newChannel(final Endpoint endpoint) {
        final ManagedChannel ch = ManagedChannelBuilder.forAddress(endpoint.getIp(), endpoint.getPort()) //
                .usePlaintext() //
                .directExecutor() //
                .maxInboundMessageSize(4 * 1024 * 1024) //todo read this value from system properties
                .build();

        LOGGER.info("Creating new channel to: {}.", endpoint);

        // The init channel state is IDLE
        notifyWhenStateChanged(ConnectivityState.IDLE, endpoint, ch);

        return ch;
    }

    private ManagedChannel removeChannel(final Endpoint endpoint) {
        return this.managedChannelPool.remove(endpoint);
    }

    private void notifyWhenStateChanged(final ConnectivityState state, final Endpoint endpoint, final ManagedChannel ch) {
        ch.notifyWhenStateChanged(state, () -> onStateChanged(endpoint, ch));
    }

    private void onStateChanged(final Endpoint endpoint, final ManagedChannel ch) {
        final ConnectivityState state = ch.getState(false);

        LOGGER.info("The channel {} is in state: {}.", endpoint, state);

        switch (state) {
            case READY:
                notifyReady(endpoint);
                notifyWhenStateChanged(ConnectivityState.READY, endpoint, ch);
                break;
            case TRANSIENT_FAILURE:
                notifyFailure(endpoint);
                notifyWhenStateChanged(ConnectivityState.TRANSIENT_FAILURE, endpoint, ch);
                break;
            case SHUTDOWN:
                notifyShutdown(endpoint);
                break;
            case CONNECTING:
                notifyWhenStateChanged(ConnectivityState.CONNECTING, endpoint, ch);
                break;
            case IDLE:
                notifyWhenStateChanged(ConnectivityState.IDLE, endpoint, ch);
                break;
        }
    }

    private void notifyReady(final Endpoint endpoint) {
        LOGGER.info("The channel {} has successfully established.", endpoint);

        clearConnFailuresCount(endpoint);
    }

    private void notifyFailure(final Endpoint endpoint) {
        LOGGER.warn("There has been some transient failure on this channel {}.", endpoint);
    }

    private void notifyShutdown(final Endpoint endpoint) {
        LOGGER.warn("This channel {} has started shutting down. Any new RPCs should fail immediately.", endpoint);
    }

    private void closeAllChannels() {
        for (final Map.Entry<Endpoint, ManagedChannel> entry : this.managedChannelPool.entrySet()) {
            final ManagedChannel ch = entry.getValue();
            LOGGER.info("Shutdown managed channel: {}, {}.", entry.getKey(), ch);
            ManagedChannelHelper.shutdownAndAwaitTermination(ch);
        }
        this.managedChannelPool.clear();
    }

    private void closeChannel(final Endpoint endpoint) {
        final ManagedChannel ch = removeChannel(endpoint);
        LOGGER.info("Close connection: {}, {}.", endpoint, ch);
        if (ch != null) {
            ManagedChannelHelper.shutdownAndAwaitTermination(ch);
        }
    }

    private boolean checkChannel(final Endpoint endpoint, final boolean createIfAbsent) {
        final ManagedChannel ch = getChannel(endpoint, createIfAbsent);

        if (ch == null) {
            return false;
        }

        return checkConnectivity(endpoint, ch);
    }

    private int incConnFailuresCount(final Endpoint endpoint) {
        return this.transientFailures.computeIfAbsent(endpoint, ep -> new AtomicInteger()).incrementAndGet();
    }

    private void clearConnFailuresCount(final Endpoint endpoint) {
        this.transientFailures.remove(endpoint);
    }

    private boolean checkConnectivity(final Endpoint endpoint, final ManagedChannel ch) {
        final ConnectivityState st = ch.getState(false);

        if (st != ConnectivityState.TRANSIENT_FAILURE && st != ConnectivityState.SHUTDOWN) {
            return true;
        }

        final int c = incConnFailuresCount(endpoint);
        if (c < RESET_CONN_THRESHOLD) {
            if (c == RESET_CONN_THRESHOLD - 1) {
                // For sub-channels that are in TRANSIENT_FAILURE state, short-circuit the backoff timer and make
                // them reconnect immediately. May also attempt to invoke NameResolver#refresh
                ch.resetConnectBackoff();
            }
            return true;
        }

        clearConnFailuresCount(endpoint);

        final ManagedChannel removedCh = removeChannel(endpoint);
        if (removedCh == null) {
            // The channel has been removed and closed by another
            return false;
        }

        LOGGER.warn("Channel[{}] in [INACTIVE] state {} times, it has been removed from the pool.", endpoint, c);

        if (removedCh != ch) {
            // Now that it's removed, close it
            ManagedChannelHelper.shutdownAndAwaitTermination(removedCh, 100);
        }

        ManagedChannelHelper.shutdownAndAwaitTermination(ch, 100);

        return false;
    }
}
