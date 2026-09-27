package io.collective.workflow;

import java.util.List;

public interface WorkFinder<T> {
    List<T> findRequested(String name);

    void markCompleted(T info);
}
