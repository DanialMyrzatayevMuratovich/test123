package io.collective.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoopWorkFinder implements WorkFinder<NoopTask> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<NoopTask> findRequested(String name) {
        logger.info("finding work.");
        return new ArrayList<>(Collections.singletonList(new NoopTask("task-name", "task-value")));
    }

    @Override
    public void markCompleted(NoopTask info) {
        logger.info("marking work complete.");
    }
}
