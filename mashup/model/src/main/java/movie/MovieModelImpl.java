package movie;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieModelImpl implements MovieModel {
    private static final MovieModelImpl instance = new MovieModelImpl();
    private final List<Movie> movies = new ArrayList<>();

    static {
        instance.addMovie("The Matrix", 1999, date(2024, 3, 12), 9);
        instance.addMovie("Inception", 2010, date(2024, 7, 5), 8);
        instance.addMovie("Interstellar", 2014, date(2025, 1, 18), 10);
    }

    private MovieModelImpl() {
    }

    public static MovieModelImpl getInstance() {
        return instance;
    }

    @Override
    public synchronized void addMovie(String title, int year, Date visualisationDate, int punctuation) {
        Movie movie = new Movie(title, year, new VisualisationInfo(visualisationDate, punctuation));

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
        Date visualisationDate = movie.getVisualisationInfo().getVisualisationDate();
        return visualisationDate.toInstant().atZone(ZoneId.systemDefault()).getYear();
    }

    private static Date date(int year, int month, int day) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
