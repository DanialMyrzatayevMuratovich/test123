package io.collective.restsupport;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class RestTemplate {
    public String get(String endpoint, String accept, BasicNameValuePair... queryParams) {
        return execute(() -> {
            try {
                URIBuilder builder = new URIBuilder(endpoint);
                for (BasicNameValuePair pair : queryParams) {
                    builder.addParameter(pair.getName(), pair.getValue());
                }
                HttpGet request = new HttpGet(builder.build());
                request.addHeader("Accept", accept);
                return request;
            } catch (URISyntaxException exception) {
                throw new IllegalArgumentException("Invalid endpoint: " + endpoint, exception);
            }
        });
    }

    public String post(String endpoint, String accept, String data) {
        return execute(() -> {
            HttpPost request = new HttpPost(endpoint);
            request.addHeader("Accept", accept);
            request.addHeader("Content-type", "application/json");
            request.setEntity(new StringEntity(data, StandardCharsets.UTF_8));
            return request;
        });
    }

    public String execute(Supplier<HttpUriRequest> requestSupplier) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return client.execute(requestSupplier.get(), new BasicResponseHandler());
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
