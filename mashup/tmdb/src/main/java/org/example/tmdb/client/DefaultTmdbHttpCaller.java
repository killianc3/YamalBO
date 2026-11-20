package org.example.tmdb.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class DefaultTmdbHttpCaller implements TmdbHttpCaller {

    private final HttpClient httpClient;
    private final String bearerToken;

    public DefaultTmdbHttpCaller() {
        this(null);
    }

    public DefaultTmdbHttpCaller(String bearerToken) {
        this.bearerToken = bearerToken;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Override
    public String get(String url) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET();

        if (bearerToken != null && !bearerToken.isBlank()) {
            builder.header("Authorization", "Bearer " + bearerToken);
        }

        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode == 404) {
            throw new FileNotFoundException("Resource not found at: " + url);
        }
        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException("HTTP request failed with status " + statusCode + ": " + response.body());
        }

        return response.body();
    }
}
