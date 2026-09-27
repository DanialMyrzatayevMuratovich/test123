package io.collective.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoopWorker implements Worker<NoopTask> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String name;

    public NoopWorker() {
        this("noop-worker");
    }

    public NoopWorker(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void execute(NoopTask task) {
        logger.info("doing work. {} {}", task.getName(), task.getValue());
    }
}
