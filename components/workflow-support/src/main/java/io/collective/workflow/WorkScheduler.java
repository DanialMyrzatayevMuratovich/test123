package io.collective.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkScheduler<T> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WorkFinder<T> finder;
    private final List<Worker<T>> workers;
    private final long delay;
    private final ScheduledExecutorService pool;
    private final ExecutorService service = Executors.newFixedThreadPool(10);

    public WorkScheduler(WorkFinder<T> finder, List<Worker<T>> workers) {
        this(finder, workers, 10L);
    }

    public WorkScheduler(WorkFinder<T> finder, List<Worker<T>> workers, long delay) {
        this.finder = finder;
        this.workers = workers;
        this.delay = delay;
        this.pool = Executors.newScheduledThreadPool(workers.size());
    }

    public void start() {
        for (Worker<T> worker : workers) {
            logger.info("scheduling worker {}", worker.getName());
            pool.scheduleWithFixedDelay(checkForWork(worker), 0, delay, TimeUnit.SECONDS);
        }
    }

    public void shutdown() {
        service.shutdown();
        pool.shutdown();
    }

    private Runnable checkForWork(Worker<T> worker) {
        return () -> {
            logger.debug("checking for work for {}", worker.getName());
            for (T task : finder.findRequested(worker.getName())) {
                logger.info("found work for {}", worker.getName());
                service.submit(() -> {
                    try {
                        worker.execute(task);
                        finder.markCompleted(task);
                        logger.info("completed work.");
                    } catch (Throwable exception) {
                        logger.error("unable to complete work", exception);
                    }
                });
            }
            logger.debug("done checking for work for {}", worker.getName());
        };
    }
}
