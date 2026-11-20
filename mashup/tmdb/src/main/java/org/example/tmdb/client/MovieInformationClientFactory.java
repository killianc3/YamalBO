package org.example.tmdb.client;

public class MovieInformationClientFactory {

    private static MovieInformationClient client;
    private static MovieInformationClientFactory instance;

    private MovieInformationClientFactory() {
    }

    public static synchronized MovieInformationClientFactory getInstance() {
        if (instance == null) {
            instance = new MovieInformationClientFactory();
        }
        return instance;
    }

    public static synchronized MovieInformationClient getClient() {
        if (client == null) {
            client = new TMDbClientImpl();
        }
        return client;
    }

    public static synchronized void setClient(MovieInformationClient newClient) {
        client = newClient;
    }

    public static synchronized void reset() {
        client = null;
    }
}
