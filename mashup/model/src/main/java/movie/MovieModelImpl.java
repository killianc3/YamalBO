package movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieModelImpl implements MovieModel {
    private static MovieModelImpl instance = null;
    private List<Movie> movies = null;

    private MovieModelImpl() {
        movies = new ArrayList<>();
        movies.add(new Movie("titlea", 1234, new VisualisationInfo(new Date(20), 2)));
    }

    public static MovieModelImpl getInstance() {
        if (instance == null) {
            instance = new MovieModelImpl();
        }

        return instance;
    }

    @Override
    public void addMovie(String title, int year, Date visualisationDate, int score) {
        movies.add(new Movie(title, year, new VisualisationInfo(visualisationDate, score)));
    }

    @Override
    public Movie findMovieByTitle(String title) throws MovieNotFoundException {
        for (var movie: movies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }

        throw new MovieNotFoundException();
    }

    @Override
    public List<Movie> findMoviesByYear(int year) {
        var moviesByYear = new ArrayList<Movie>();

        for (var movie: movies) {
            if (movie.getYear() == year) {
                moviesByYear.add(movie);
            }
        }

        return moviesByYear;
    }
}
