package io.collective.restsupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public abstract class BasicHandler extends AbstractHandler {
    private final ObjectMapper mapper;

    public BasicHandler() {
        this(new ObjectMapper());
    }

    public BasicHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void post(String uri, List<String> supportedMediaTypes, Request request,
                     HttpServletResponse servletResponse, Runnable block) {
        handleRequest(HttpMethod.POST, HttpServletResponse.SC_CREATED, uri,
                supportedMediaTypes, request, servletResponse, block);
    }

    public void get(String uri, List<String> supportedMediaTypes, Request request,
                    HttpServletResponse servletResponse, Runnable block) {
        handleRequest(HttpMethod.GET, HttpServletResponse.SC_OK, uri,
                supportedMediaTypes, request, servletResponse, block);
    }

    private void handleRequest(HttpMethod method, int successStatus, String uri,
                               List<String> supportedMediaTypes, Request request,
                               HttpServletResponse servletResponse, Runnable block) {
        if (!method.toString().equals(request.getMethod()) || !uri.equals(request.getRequestURI())) {
            return;
        }

        String acceptedMediaType = request.getHeader("Accept");
        if (acceptedMediaType == null) {
            return;
        }

        for (String supportedMediaType : supportedMediaTypes) {
            if (acceptedMediaType.contains(supportedMediaType)) {
                servletResponse.setContentType(supportedMediaType);
                try {
                    block.run();
                    servletResponse.setStatus(successStatus);
                } catch (UncheckedIOException exception) {
                    servletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                request.setHandled(true);
                return;
            }
        }
    }

    protected void writeJsonBody(HttpServletResponse servletResponse, Object subject) {
        try {
            mapper.writeValue(servletResponse.getOutputStream(), subject);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
