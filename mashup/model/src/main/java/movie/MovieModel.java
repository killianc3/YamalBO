package movie;

import java.util.Date;
import java.util.List;

public interface MovieModel {
    public void addMovie(String title, int year, Date visualisationDate, int score);
    public Movie findMovieByTitle(String title) throws MovieNotFoundException;
    public List<Movie> findMoviesByYear(int year);
}
