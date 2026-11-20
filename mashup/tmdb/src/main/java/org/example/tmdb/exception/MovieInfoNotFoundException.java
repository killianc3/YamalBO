package org.example.tmdb.exception;

public class MovieInfoNotFoundException extends Exception {

    public MovieInfoNotFoundException() {
        super();
    }

    public MovieInfoNotFoundException(String message) {
        super(message);
    }

    public MovieInfoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
