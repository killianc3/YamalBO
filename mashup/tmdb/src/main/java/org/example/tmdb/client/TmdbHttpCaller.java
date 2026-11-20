package org.example.tmdb.client;

import java.io.IOException;

public interface TmdbHttpCaller {

    /**
     * Executes an HTTP GET request to the given URL and returns the response body as a String.
     *
     * @param url the full URL to query
     * @return the response body string
     * @throws IOException          if an I/O error occurs or the HTTP status code indicates failure (4xx, 5xx)
     * @throws InterruptedException if the request is interrupted
     */
    String get(String url) throws IOException, InterruptedException;
}
