package movie;

public class MovieNotFoundException extends Exception {
    public MovieNotFoundException() {
        super("Movie not found");
    }

    public MovieNotFoundException(String title) {
        super("Movie not found: " + title);
    }
}
