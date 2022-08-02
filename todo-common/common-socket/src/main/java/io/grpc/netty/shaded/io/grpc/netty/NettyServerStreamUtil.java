package io.grpc.netty.shaded.io.grpc.netty;

import com.epam.common.socket.util.ReferenceFieldUpdater;
import com.epam.common.socket.util.UnsafeUtil;
import io.grpc.netty.shaded.io.netty.channel.Channel;

public class NettyServerStreamUtil {

    public static final String WRITE_QUEUE_FIELD_NAME = "writeQueue";

    public static Channel getChannel(NettyServerStream nettyServerStream) {
        // get write queue from WriteQueue field
        ReferenceFieldUpdater<NettyServerStream, WriteQueue> referenceFieldUpdater;
        try {
            if (UnsafeUtil.hasUnsafe()) {
                referenceFieldUpdater = new UnsafeReferenceFieldUpdater<>(UnsafeUtil.getUnsafeAccessor().getUnsafe(),
                        NettyServerStream.class, WRITE_QUEUE_FIELD_NAME);
            } else {
                referenceFieldUpdater =  new ReflectionReferenceFieldUpdater<>(NettyServerStream.class, WRITE_QUEUE_FIELD_NAME);
            }

            WriteQueue writeQueue = referenceFieldUpdater.get(nettyServerStream);
            if (writeQueue == null) {
                throw new NoSuchFieldException(WRITE_QUEUE_FIELD_NAME);
            }

            ReferenceFieldUpdater<WriteQueue, Channel> channelReferenceFieldUpdater;
            // get channel from WriteQueue
            if (UnsafeUtil.hasUnsafe()) {
                channelReferenceFieldUpdater = new UnsafeReferenceFieldUpdater<>(UnsafeUtil.getUnsafeAccessor().getUnsafe(),
                        WriteQueue.class, "channel");
            } else {
                channelReferenceFieldUpdater =  new ReflectionReferenceFieldUpdater<>(WriteQueue.class, "channel");
            }

            return channelReferenceFieldUpdater.get(writeQueue);
        } catch (final Exception t) {
            throw new RuntimeException(t);
        }
    }
}
