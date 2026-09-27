package test.collective.workflow;

import io.collective.workflow.NoopTask;
import io.collective.workflow.NoopWorkFinder;
import io.collective.workflow.NoopWorker;
import io.collective.workflow.WorkScheduler;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class WorkSchedulerTest {
    @Test
    public void testScheduler() throws Exception {
        NoopWorkFinder finder = new NoopWorkFinder();
        NoopWorker worker = spy(new NoopWorker());
        WorkScheduler<NoopTask> scheduler = new WorkScheduler<>(finder, Collections.singletonList(worker));

        scheduler.start();
        try {
            verify(worker, timeout(2000).times(1)).execute(any());
        } finally {
            scheduler.shutdown();
        }
    }
}
