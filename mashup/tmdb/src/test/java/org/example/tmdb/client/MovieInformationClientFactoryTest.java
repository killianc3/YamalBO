package org.example.tmdb.client;

import org.example.tmdb.dto.MovieInfoDto;
import org.example.tmdb.exception.MovieInfoNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieInformationClientFactoryTest {

    @AfterEach
    void tearDown() {
        MovieInformationClientFactory.reset();
    }

    @Test
    void getClient_returnsNonNullSingletonInstance() {
        MovieInformationClient client1 = MovieInformationClientFactory.getClient();
        MovieInformationClient client2 = MovieInformationClientFactory.getClient();

        assertNotNull(client1);
        assertSame(client1, client2, "Factory must behave as a singleton and return the same instance");
    }

    @Test
    void setClient_allowsCustomMockInjection() throws Exception {
        MovieInformationClient mockClient = title -> new MovieInfoDto(title, null, null, 2000, null);

        MovieInformationClientFactory.setClient(mockClient);
        MovieInformationClient retrieved = MovieInformationClientFactory.getClient();

        assertSame(mockClient, retrieved);
        MovieInfoDto result = retrieved.findMovieInformation("Custom Title");
        assertEquals("Custom Title", result.getTitle());
    }
}
