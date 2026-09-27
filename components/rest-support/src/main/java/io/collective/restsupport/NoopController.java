package io.collective.restsupport;

import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class NoopController extends BasicHandler {
    @Override
    public void handle(String target, Request request, HttpServletRequest servletRequest,
                       HttpServletResponse servletResponse) {
        servletResponse.setContentType("text/html; charset=UTF-8");
        try {
            servletResponse.getOutputStream().write("Noop!".getBytes(StandardCharsets.UTF_8));
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
        servletResponse.setStatus(HttpServletResponse.SC_OK);
        request.setHandled(true);
    }
}
