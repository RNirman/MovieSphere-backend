package com.example.moviesphere.service;

import com.example.moviesphere.model.Movie;
import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> findAllMovies();
    Optional<Movie> findMovieById(Long id);
    Movie saveMovie(Movie movie);
    void deleteMovie(Long id);
    Movie updateMovie(Movie movie);
    // Add methods for integration like: Movie createMovieFromExternalApi(String imdbId);
}
