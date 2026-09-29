package org.example.thrift.service;

import org.example.thrift.gen.MovieDto;
import org.example.thrift.gen.ServiceMovieNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovieServiceThriftImplTest {

    private MovieServiceThriftImpl service;

    @BeforeEach
    void setUp() {
        service = new MovieServiceThriftImpl(new FakeMovieModel());
    }

    @Test
    void addMovie_thenFindByTitle_returnsIt() throws Exception {
        MovieDto dto = new MovieDto("The Matrix", (short) 1999, "2025-01-10", (short) 9);

        service.addMovie(dto);
        MovieDto found = service.findMovieByTitle("The Matrix");

        assertEquals("The Matrix", found.getTitle());
        assertEquals(1999, found.getYear());
        assertEquals("2025-01-10", found.getVisualisationDate());
        assertEquals(9, found.getPoints());
    }

    @Test
    void findMovieByTitle_unknownTitle_throwsServiceException() {
        assertThrows(ServiceMovieNotFoundException.class,
                () -> service.findMovieByTitle("Unknown"));
    }

    @Test
    void findMoviesByYear_returnsOnlyMoviesSeenThatYear() throws Exception {
        service.addMovie(new MovieDto("The Matrix", (short) 1999, "2024-01-10", (short) 9));
        service.addMovie(new MovieDto("Inception", (short) 2010, "2025-02-20", (short) 8));

        List<MovieDto> seenIn2024 = service.findMoviesByYear((short) 2024);

        assertEquals(1, seenIn2024.size());
        assertEquals("The Matrix", seenIn2024.get(0).getTitle());
    }
}
