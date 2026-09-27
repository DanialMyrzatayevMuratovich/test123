package io.collective.workflow;

import java.util.Objects;

public class NoopTask {
    private final String name;
    private final String value;

    public NoopTask(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String component1() {
        return name;
    }

    public String component2() {
        return value;
    }

    public NoopTask copy(String name, String value) {
        return new NoopTask(name, value);
    }

    @Override
    public boolean equals(Object subject) {
        if (this == subject) return true;
        if (!(subject instanceof NoopTask)) return false;
        NoopTask task = (NoopTask) subject;
        return Objects.equals(name, task.name) && Objects.equals(value, task.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "NoopTask(name=" + name + ", value=" + value + ")";
    }
}
