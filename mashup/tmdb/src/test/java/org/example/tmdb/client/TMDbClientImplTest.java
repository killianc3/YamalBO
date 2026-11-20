package org.example.tmdb.client;

import org.example.tmdb.dto.CharacterDto;
import org.example.tmdb.dto.MovieInfoDto;
import org.example.tmdb.exception.MovieInfoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TMDbClientImplTest {

    private Map<String, String> responses;
    private TmdbHttpCaller mockHttpCaller;
    private TMDbClientImpl client;

    @BeforeEach
    void setUp() {
        responses = new HashMap<>();

        mockHttpCaller = url -> {
            for (Map.Entry<String, String> entry : responses.entrySet()) {
                if (url.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
            throw new FileNotFoundException("Mock response not found for URL: " + url);
        };

        TmdbConfig config = new TmdbConfig();
        config.setApiKey("test_api_key");
        config.setMaxCharacters(5);

        client = new TMDbClientImpl(config, mockHttpCaller);
    }

    @Test
    void findMovieInformation_success() throws Exception {
        // Mock responses matching project.pdf Section 3
        responses.put("/search/movie", """
                {
                  "page": 1,
                  "results": [
                    {
                      "id": 603,
                      "title": "The Matrix",
                      "release_date": "1999-03-30"
                    }
                  ],
                  "total_results": 1
                }
                """);

        responses.put("/movie/603/credits", """
                {
                  "id": 603,
                  "cast": [
                    {
                      "gender": 2, "id": 6384, "name": "Keanu Reeves", "original_name": "Keanu Reeves",
                      "profile_path": "/8RZLOyYGsoRe9p44q3xin9QkMHv.jpg", "character": "Neo"
                    },
                    {
                      "gender": 2, "id": 2975, "name": "Laurence Fishburne", "original_name": "Laurence Fishburne",
                      "profile_path": "/2GbXERENPpl5MmlqOLlPVaVtifD.jpg", "character": "Morpheus"
                    },
                    {
                      "gender": 1, "id": 530, "name": "Carrie-Anne Moss", "original_name": "Carrie-Anne Moss",
                      "profile_path": "/xD4jTA3KmVp5Rq3aHcymL9DUGjD.jpg", "character": "Trinity"
                    }
                  ]
                }
                """);

        responses.put("/movie/603", """
                {
                  "adult": false,
                  "backdrop_path": "/lrtSb1skJayPydZk0OSMAKjBOVe.jpg",
                  "budget": 63000000,
                  "genres": [ { "id": 28, "name": "Action"}, {"id": 878, "name": "Science Fiction"} ],
                  "id": 603,
                  "overview": "Set in the 22nd century...",
                  "popularity": 48.6817,
                  "poster_path": "/aOIuZAjPaRIE6CMzbazvcHuHXDc.jpg",
                  "title": "The Matrix",
                  "release_date": "1999-03-30"
                }
                """);

        responses.put("/person/6384", """
                {
                  "birthday": "1964-09-02",
                  "deathday": null,
                  "id": 6384,
                  "name": "Keanu Reeves",
                  "place_of_birth": "Beirut, Lebanon",
                  "profile_path": "/8RZLOyYGsoRe9p44q3xin9QkMHv.jpg"
                }
                """);

        responses.put("/person/2975", """
                {
                  "birthday": "1961-07-30",
                  "deathday": null,
                  "id": 2975,
                  "name": "Laurence Fishburne",
                  "place_of_birth": "Augusta, Georgia, USA",
                  "profile_path": "/2GbXERENPpl5MmlqOLlPVaVtifD.jpg"
                }
                """);

        responses.put("/person/530", """
                {
                  "birthday": "1967-08-21",
                  "deathday": null,
                  "id": 530,
                  "name": "Carrie-Anne Moss",
                  "place_of_birth": "Burnaby, British Columbia, Canada",
                  "profile_path": "/xD4jTA3KmVp5Rq3aHcymL9DUGjD.jpg"
                }
                """);

        MovieInfoDto movie = client.findMovieInformation("The Matrix");

        assertNotNull(movie);
        assertEquals("The Matrix", movie.getTitle());
        assertEquals(1999, movie.getYear());
        assertEquals(List.of("Action", "Science Fiction"), movie.getGenre());
        assertEquals(URI.create("https://image.tmdb.org/t/p/w500/aOIuZAjPaRIE6CMzbazvcHuHXDc.jpg").toURL(), movie.getPoster_path());

        List<CharacterDto> characters = movie.getCharacters();
        assertNotNull(characters);
        assertEquals(3, characters.size());

        // Verify Keanu Reeves / Neo
        CharacterDto neo = characters.get(0);
        assertEquals("Neo", neo.getCharacterName());
        assertEquals("Keanu Reeves", neo.getActorName());
        assertEquals("1964-09-02", neo.getBirthday());
        assertNull(neo.getDeathday());
        assertEquals("Beirut, Lebanon", neo.getPlaceOfBirth());
        assertEquals(URI.create("https://image.tmdb.org/t/p/w500/8RZLOyYGsoRe9p44q3xin9QkMHv.jpg").toURL(), neo.getImgUrl());

        // Verify Morpheus
        CharacterDto morpheus = characters.get(1);
        assertEquals("Morpheus", morpheus.getCharacterName());
        assertEquals("Laurence Fishburne", morpheus.getActorName());

        // Verify Trinity
        CharacterDto trinity = characters.get(2);
        assertEquals("Trinity", trinity.getCharacterName());
        assertEquals("Carrie-Anne Moss", trinity.getActorName());
    }

    @Test
    void findMovieInformation_emptyTitle_throwsException() {
        assertThrows(MovieInfoNotFoundException.class, () -> client.findMovieInformation(""));
        assertThrows(MovieInfoNotFoundException.class, () -> client.findMovieInformation("   "));
        assertThrows(MovieInfoNotFoundException.class, () -> client.findMovieInformation(null));
    }

    @Test
    void findMovieInformation_movieNotFound_throwsException() {
        responses.put("/search/movie", """
                {
                  "page": 1,
                  "results": [],
                  "total_results": 0
                }
                """);

        assertThrows(MovieInfoNotFoundException.class, () -> client.findMovieInformation("NonExistentMovie999"));
    }

    @Test
    void findMovieInformation_networkError_throwsException() {
        TmdbHttpCaller failingCaller = url -> {
            throw new IOException("Simulated network timeout");
        };

        TMDbClientImpl failingClient = new TMDbClientImpl(new TmdbConfig(), failingCaller);
        assertThrows(MovieInfoNotFoundException.class, () -> failingClient.findMovieInformation("The Matrix"));
    }
}
