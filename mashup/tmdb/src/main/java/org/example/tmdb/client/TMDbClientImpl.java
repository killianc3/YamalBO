package org.example.tmdb.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tmdb.dto.CharacterDto;
import org.example.tmdb.dto.MovieInfoDto;
import org.example.tmdb.exception.MovieInfoNotFoundException;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TMDbClientImpl implements MovieInformationClient {

    private final String apiKey;
    private final String baseUrl;
    private final String imageBaseUrl;
    private final int maxCharacters;
    private final TmdbHttpCaller httpCaller;
    private final ObjectMapper objectMapper;

    public TMDbClientImpl() {
        this(new TmdbConfig());
    }

    public TMDbClientImpl(TmdbConfig config) {
        this(config, createDefaultHttpCaller(config.getApiKey()));
    }

    public TMDbClientImpl(String apiKey) {
        this(createConfigWithKey(apiKey));
    }

    public TMDbClientImpl(String apiKey, TmdbHttpCaller httpCaller) {
        this(createConfigWithKey(apiKey), httpCaller);
    }

    public TMDbClientImpl(TmdbConfig config, TmdbHttpCaller httpCaller) {
        this.apiKey = config.getApiKey();
        this.baseUrl = config.getBaseUrl();
        this.imageBaseUrl = config.getImageBaseUrl();
        this.maxCharacters = config.getMaxCharacters();
        this.httpCaller = httpCaller != null ? httpCaller : createDefaultHttpCaller(this.apiKey);
        this.objectMapper = new ObjectMapper();
    }

    private static TmdbConfig createConfigWithKey(String apiKey) {
        TmdbConfig config = new TmdbConfig();
        if (apiKey != null) {
            config.setApiKey(apiKey);
        }
        return config;
    }

    private static TmdbHttpCaller createDefaultHttpCaller(String apiKey) {
        if (apiKey != null && apiKey.startsWith("eyJ")) {
            return new DefaultTmdbHttpCaller(apiKey);
        }
        return new DefaultTmdbHttpCaller();
    }

    @Override
    public MovieInfoDto findMovieInformation(String title) throws MovieInfoNotFoundException {
        if (title == null || title.isBlank()) {
            throw new MovieInfoNotFoundException("Movie title cannot be empty");
        }

        try {
            // 1. Search for movie by title to get its ID
            int movieId = searchMovieIdByTitle(title.trim());

            // 2. Retrieve detailed movie info
            MovieInfoDto movieInfo = fetchMovieDetails(movieId);

            // 3. Retrieve credits and actors info
            List<CharacterDto> characters = fetchMovieCharacters(movieId);
            movieInfo.setCharacters(characters);

            return movieInfo;
        } catch (MovieInfoNotFoundException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw new MovieInfoNotFoundException("Movie not found: " + title, e);
        } catch (Exception e) {
            throw new MovieInfoNotFoundException("Failed to retrieve movie information for: " + title, e);
        }
    }

    private int searchMovieIdByTitle(String title) throws Exception {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String url = buildUrl(baseUrl + "/search/movie?query=" + encodedTitle);

        String jsonResponse = httpCaller.get(url);
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode resultsNode = rootNode.path("results");

        if (!resultsNode.isArray() || resultsNode.isEmpty()) {
            throw new MovieInfoNotFoundException("No movie found with title: " + title);
        }

        JsonNode firstMovie = resultsNode.get(0);
        return firstMovie.path("id").asInt();
    }

    private MovieInfoDto fetchMovieDetails(int movieId) throws Exception {
        String url = buildUrl(baseUrl + "/movie/" + movieId);
        String jsonResponse = httpCaller.get(url);
        JsonNode movieNode = objectMapper.readTree(jsonResponse);

        String title = movieNode.path("title").asText();

        List<String> genres = new ArrayList<>();
        JsonNode genresNode = movieNode.path("genres");
        if (genresNode.isArray()) {
            for (JsonNode g : genresNode) {
                if (g.hasNonNull("name")) {
                    genres.add(g.path("name").asText());
                }
            }
        }

        String posterPath = movieNode.hasNonNull("poster_path") ? movieNode.path("poster_path").asText() : null;
        URL posterUrl = toImageUrl(posterPath);

        int releaseYear = 0;
        if (movieNode.hasNonNull("release_date")) {
            String releaseDate = movieNode.path("release_date").asText();
            if (releaseDate.length() >= 4) {
                try {
                    releaseYear = Integer.parseInt(releaseDate.substring(0, 4));
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return new MovieInfoDto(title, genres, posterUrl, releaseYear, new ArrayList<>());
    }

    private List<CharacterDto> fetchMovieCharacters(int movieId) {
        List<CharacterDto> characters = new ArrayList<>();

        try {
            String url = buildUrl(baseUrl + "/movie/" + movieId + "/credits");
            String jsonResponse = httpCaller.get(url);
            JsonNode creditsNode = objectMapper.readTree(jsonResponse);
            JsonNode castNode = creditsNode.path("cast");

            if (!castNode.isArray()) {
                return characters;
            }

            int count = 0;
            for (JsonNode castItem : castNode) {
                if (maxCharacters > 0 && count >= maxCharacters) {
                    break;
                }

                String characterName = castItem.path("character").asText("");
                String actorName = castItem.hasNonNull("name")
                        ? castItem.path("name").asText()
                        : castItem.path("original_name").asText("");
                int actorId = castItem.path("id").asInt();
                String profilePath = castItem.hasNonNull("profile_path")
                        ? castItem.path("profile_path").asText()
                        : null;

                // Biographical information
                PersonBio bio = fetchPersonBio(actorId);

                // If person endpoint provides a profile_path, use it as fallback
                if (profilePath == null && bio != null && bio.profilePath != null) {
                    profilePath = bio.profilePath;
                }

                URL imgUrl = toImageUrl(profilePath);
                String birthday = bio != null ? bio.birthday : null;
                String deathday = bio != null ? bio.deathday : null;
                String placeOfBirth = bio != null ? bio.placeOfBirth : null;

                CharacterDto characterDto = new CharacterDto(
                        characterName,
                        imgUrl,
                        actorName,
                        birthday,
                        deathday,
                        placeOfBirth
                );

                characters.add(characterDto);
                count++;
            }
        } catch (Exception ignored) {
            // If credit retrieval encounters an issue, return whatever characters were collected
        }

        return characters;
    }

    private PersonBio fetchPersonBio(int personId) {
        if (personId <= 0) {
            return null;
        }

        try {
            String url = buildUrl(baseUrl + "/person/" + personId);
            String jsonResponse = httpCaller.get(url);
            JsonNode personNode = objectMapper.readTree(jsonResponse);

            PersonBio bio = new PersonBio();
            bio.birthday = personNode.hasNonNull("birthday") ? personNode.path("birthday").asText() : null;
            bio.deathday = personNode.hasNonNull("deathday") ? personNode.path("deathday").asText() : null;
            bio.placeOfBirth = personNode.hasNonNull("place_of_birth") ? personNode.path("place_of_birth").asText() : null;
            bio.profilePath = personNode.hasNonNull("profile_path") ? personNode.path("profile_path").asText() : null;
            return bio;
        } catch (Exception e) {
            return null;
        }
    }

    private String buildUrl(String url) {
        // If apiKey is provided and is not a Bearer JWT, append it as a query parameter
        if (apiKey != null && !apiKey.isBlank() && !apiKey.startsWith("eyJ")) {
            String separator = url.contains("?") ? "&" : "?";
            return url + separator + "api_key=" + apiKey;
        }
        return url;
    }

    private URL toImageUrl(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return null;
        }
        try {
            if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
                return URI.create(relativePath).toURL();
            }
            String separator = relativePath.startsWith("/") ? "" : "/";
            return URI.create(imageBaseUrl + separator + relativePath).toURL();
        } catch (Exception e) {
            return null;
        }
    }

    private static class PersonBio {
        String birthday;
        String deathday;
        String placeOfBirth;
        String profilePath;
    }
}
