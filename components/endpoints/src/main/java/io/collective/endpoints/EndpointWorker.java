package io.collective.endpoints;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.collective.articles.ArticleDataGateway;
import io.collective.restsupport.RestTemplate;
import io.collective.rss.RSS;
import io.collective.workflow.Worker;

import java.io.IOException;

public class EndpointWorker implements Worker<EndpointTask> {
    private final RestTemplate template;
    private final ArticleDataGateway gateway;

    public EndpointWorker(RestTemplate template, ArticleDataGateway gateway) {
        this.template = template;
        this.gateway = gateway;
    }

    @Override
    public String getName() {
        return "ready";
    }

    @Override
    public void execute(EndpointTask task) throws IOException {
        String response = template.get(task.getEndpoint(), task.getAccept());
        RSS rss = new XmlMapper().readValue(response, RSS.class);
        gateway.clear();
        if (rss.getChannel().getItem() != null) {
            rss.getChannel().getItem().forEach(item -> gateway.save(item.getTitle()));
        }
    }
}
