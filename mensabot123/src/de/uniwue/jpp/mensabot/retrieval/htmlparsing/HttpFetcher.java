package de.uniwue.jpp.mensabot.retrieval.htmlparsing;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.retrieval.Fetcher;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpFetcher implements Fetcher {
    HttpClient client;
    String url;
    public HttpFetcher(HttpClient client, String url) {
        this.client = client;
        this.url = url;
    }

    @Override
    public OptionalWithMessage<String> fetchCurrentData() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() > 199 && response.statusCode() < 300)
                return OptionalWithMessage.of(response.body().toString());
            else
                return OptionalWithMessage.ofMsg("Error code returned: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            return OptionalWithMessage.ofMsg("Error during execution of request!");
        }
    }

}

