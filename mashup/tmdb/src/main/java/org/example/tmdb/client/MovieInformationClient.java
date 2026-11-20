package org.example.tmdb.client;

import org.example.tmdb.dto.MovieInfoDto;
import org.example.tmdb.exception.MovieInfoNotFoundException;

public interface MovieInformationClient {

    MovieInfoDto findMovieInformation(String title) throws MovieInfoNotFoundException;
}
