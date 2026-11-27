package movie;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovieModelImplTest {

    private final MovieModel model = MovieModelFactory.getModel();

    @Test
    void factory_returnsSingleton() {
        assertSame(model, MovieModelFactory.getModel());
    }

    @Test
    void model_isPopulatedWhenLoaded() throws Exception {
        assertEquals(1999, model.findMovieByTitle("The Matrix").getYear());
    }

    @Test
    void addMovie_existingTitle_updatesInsteadOfDuplicating() throws Exception {
        model.addMovie("Update Me", 2000, date(2023, 1, 1), 5);
        model.addMovie("Update Me", 2001, date(2023, 6, 1), 9);

        Movie movie = model.findMovieByTitle("Update Me");
        assertEquals(2001, movie.getYear());
        assertEquals(9, movie.getVisualisationInfo().getPunctuation());
        assertEquals(1, model.findMoviesByYear(2023).stream()
                .filter(m -> m.getTitle().equals("Update Me"))
                .count());
    }

    @Test
    void findMovieByTitle_unknownTitle_throws() {
        assertThrows(MovieNotFoundException.class, () -> model.findMovieByTitle("Does Not Exist"));
    }

    @Test
    void findMoviesByYear_usesViewingYearNotReleaseYear() {
        model.addMovie("Old Film", 1950, date(2022, 4, 10), 7);

        assertTrue(model.findMoviesByYear(2022).stream().anyMatch(m -> m.getTitle().equals("Old Film")));
        assertTrue(model.findMoviesByYear(1950).stream().noneMatch(m -> m.getTitle().equals("Old Film")));
    }

    private static Date date(int year, int month, int day) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
