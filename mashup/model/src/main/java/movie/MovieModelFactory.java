package movie;

public class MovieModelFactory {
    public static MovieModel getModel() {
        return MovieModelImpl.getInstance();
    }
}
