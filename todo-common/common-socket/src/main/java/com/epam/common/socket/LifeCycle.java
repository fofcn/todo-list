package com.epam.common.socket;

/**
 * Define service life cycle.
 * @param <T>
 */
public interface LifeCycle<T> {

    /**
     * Initialize the service with configuration.
     * @param config Service's configuration.
     * @return true if success, false otherwise.
     */
    boolean init(T config);

    /**
     * Start the service.
     */
    void start();

    /**
     * test if server is started.
     * @return true if server have started, false otherwise.
     */
    boolean isStarted();

    /**
     * Shutdown the service.
     */
    void shutdown();
}
