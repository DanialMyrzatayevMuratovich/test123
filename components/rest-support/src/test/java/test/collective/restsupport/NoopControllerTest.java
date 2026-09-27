package test.collective.restsupport;

import io.collective.restsupport.BasicApp;
import io.collective.restsupport.NoopController;
import io.collective.restsupport.RestTemplate;
import org.eclipse.jetty.server.handler.HandlerList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NoopControllerTest {
    private final RestTemplate template = new RestTemplate();

    private final BasicApp app = new BasicApp(8888) {
        @Override
        protected HandlerList handlerList() {
            HandlerList list = new HandlerList();
            list.addHandler(new NoopController());
            return list;
        }
    };

    @Before
    public void setUp() {
        app.start();
    }

    @After
    public void tearDown() {
        app.stop();
    }

    @Test
    public void testGet() {
        assertEquals("Noop!", template.get("http://localhost:8888/", "*/*"));
    }
}
