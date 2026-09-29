package org.example.thrift.service;

import movie.Movie;
import movie.MovieModel;
import movie.MovieNotFoundException;
import org.example.thrift.gen.MovieDto;
import org.example.thrift.gen.MovieService;
import org.example.thrift.gen.ServiceMovieNotFoundException;
import org.example.thrift.mapper.MovieDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MovieServiceThriftImpl implements MovieService.Iface {

    private final MovieModel model;

    public MovieServiceThriftImpl(MovieModel model) {
        this.model = model;
    }

    @Override
    public void addMovie(MovieDto movie) {
        model.addMovie(
                movie.getTitle(),
                movie.getYear(),
                MovieDtoMapper.parseDate(movie.getVisualisationDate()),
                movie.getPoints());
    }

    @Override
    public MovieDto findMovieByTitle(String title) throws ServiceMovieNotFoundException {
        try {
            Movie movie = model.findMovieByTitle(title);
            return MovieDtoMapper.toDto(movie);
        } catch (MovieNotFoundException e) {
            throw new ServiceMovieNotFoundException(title);
        }
    }

    @Override
    public List<MovieDto> findMoviesByYear(short year) {
        return model.findMoviesByYear(year).stream()
                .map(MovieDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
