package org.example.tmdb.client;

import java.io.InputStream;
import java.util.Properties;

public class TmdbConfig {

    private static final String DEFAULT_BASE_URL = "https://api.themoviedb.org/3";
    private static final String DEFAULT_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final int DEFAULT_MAX_CHARACTERS = 10;

    private String apiKey;
    private String baseUrl = DEFAULT_BASE_URL;
    private String imageBaseUrl = DEFAULT_IMAGE_BASE_URL;
    private int maxCharacters = DEFAULT_MAX_CHARACTERS;

    public TmdbConfig() {
        loadDefaults();
    }

    private void loadDefaults() {
        Properties properties = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/tmdb.properties")) {
            if (is != null) {
                properties.load(is);
            }
        } catch (Exception ignored) {
        }

        // Priority 1: System environment variable
        String envKey = System.getenv("TMDB_API_KEY");
        if (envKey == null || envKey.isBlank()) {
            envKey = System.getenv("TMDB_TOKEN");
        }

        // Priority 2: System property
        String sysPropKey = System.getProperty("tmdb.api.key");

        // Priority 3: properties file
        String fileKey = properties.getProperty("tmdb.api.key");

        if (envKey != null && !envKey.isBlank()) {
            this.apiKey = envKey.trim();
        } else if (sysPropKey != null && !sysPropKey.isBlank()) {
            this.apiKey = sysPropKey.trim();
        } else if (fileKey != null && !fileKey.isBlank()) {
            this.apiKey = fileKey.trim();
        } else {
            this.apiKey = "";
        }

        String baseUrlProp = System.getProperty("tmdb.api.base_url", properties.getProperty("tmdb.api.base_url"));
        if (baseUrlProp != null && !baseUrlProp.isBlank()) {
            this.baseUrl = baseUrlProp.trim();
        }

        String imgUrlProp = System.getProperty("tmdb.image.base_url", properties.getProperty("tmdb.image.base_url"));
        if (imgUrlProp != null && !imgUrlProp.isBlank()) {
            this.imageBaseUrl = imgUrlProp.trim();
        }

        String maxCharsProp = System.getProperty("tmdb.max.characters", properties.getProperty("tmdb.max.characters"));
        if (maxCharsProp != null && !maxCharsProp.isBlank()) {
            try {
                this.maxCharacters = Integer.parseInt(maxCharsProp.trim());
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public void setImageBaseUrl(String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }
}
