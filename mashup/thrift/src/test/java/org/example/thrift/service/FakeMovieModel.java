package org.example.thrift.service;

import movie.Movie;
import movie.MovieModel;
import movie.MovieNotFoundException;
import movie.VisualisationInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class FakeMovieModel implements MovieModel {

    private final List<Movie> movies = new ArrayList<>();

    @Override
    public void addMovie(String title, int year, Date visualisationDate, int score) {
        movies.add(new Movie(title, year, new VisualisationInfo(visualisationDate, score)));
    }

    @Override
    public Movie findMovieByTitle(String title) throws MovieNotFoundException {
        for (Movie m : movies) {
            if (m.getTitle().equals(title)) {
                return m;
            }
        }
        throw new MovieNotFoundException(title);
    }

    @Override
    public List<Movie> findMoviesByYear(int year) {
        List<Movie> result = new ArrayList<>();
        for (Movie m : movies) {
            if (m.getYear() == year) {
                result.add(m);
            }
        }
        return result;
    }
}
