package movie;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieModelImpl implements MovieModel {
    private static final MovieModelImpl instance = new MovieModelImpl();
    private static final List<Movie> movies = new ArrayList<>();

    static {
        movies.add(new Movie("titlea", 1234, new VisualisationInfo(new Date(20), 2)));
    }

    private MovieModelImpl() {
    }

    public static MovieModelImpl getInstance() {
        return instance;
    }

    @Override
    public synchronized void addMovie(String title, int year, Date visualisationDate, int score) {
        Movie movie = new Movie(title, year, new VisualisationInfo(visualisationDate, score));

        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getTitle().equals(title)) {
                movies.set(i, movie);
                return;
            }
        }

        movies.add(movie);
    }

    @Override
    public synchronized Movie findMovieByTitle(String title) throws MovieNotFoundException {
        for (var movie: movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }

        throw new MovieNotFoundException(title);
    }

    @Override
    public synchronized List<Movie> findMoviesByYear(int year) {
        var moviesByYear = new ArrayList<Movie>();

        for (var movie: movies) {
            if (viewingYear(movie) == year) {
                moviesByYear.add(movie);
            }
        }

        return moviesByYear;
    }

    private static int viewingYear(Movie movie) {
        return movie.getInfo().getDate().toInstant().atZone(ZoneId.systemDefault()).getYear();
    }
}
