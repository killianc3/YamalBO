package org.example.thrift.mapper;

import movie.Movie;
import movie.VisualisationInfo;
import org.example.thrift.gen.MovieDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class MovieDtoMapper {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private MovieDtoMapper() {
    }

    public static MovieDto toDto(Movie movie) {
        VisualisationInfo info = movie.getVisualisationInfo();
        return new MovieDto(
                movie.getTitle(),
                (short) movie.getYear(),
                formatDate(info.getVisualisationDate()),
                (short) info.getPunctuation());
    }

    public static Date parseDate(String visualisationDate) {
        try {
            return new SimpleDateFormat(DATE_PATTERN).parse(visualisationDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date: " + visualisationDate, e);
        }
    }

    private static String formatDate(Date date) {
        return new SimpleDateFormat(DATE_PATTERN).format(date);
    }
}
