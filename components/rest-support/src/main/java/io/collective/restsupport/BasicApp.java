package io.collective.restsupport;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasicApp {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Server server;

    public BasicApp(int port) {
        HandlerList list = handlerList();
        server = new Server(port);
        server.setHandler(list);
        server.setStopAtShutdown(true);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (server.isRunning()) {
                    stop();
                }
                logger.info("App shutdown.");
            } catch (RuntimeException exception) {
                logger.info("Error shutting down app.", exception);
            }
        }));
    }

    protected abstract HandlerList handlerList();

    public void start() {
        logger.info("App started.");
        try {
            server.start();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to start app.", exception);
        }
    }

    public void stop() {
        logger.info("App stopped.");
        try {
            server.stop();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to stop app.", exception);
        }
    }
}
