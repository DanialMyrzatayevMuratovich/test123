package io.collective.workflow;

public interface Worker<T> {
    String getName();

    void execute(T task) throws Exception;
}
