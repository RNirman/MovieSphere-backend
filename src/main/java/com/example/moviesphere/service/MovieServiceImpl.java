package com.example.moviesphere.service;

import com.example.moviesphere.model.Movie;
import com.example.moviesphere.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Optional<Movie> findMovieById(Long id) {
        return movieRepository.findById(id);
    }

    @Override
    public List<Movie> searchMovies(String title) {
        if (title != null && !title.trim().isEmpty()) {
            return movieRepository.findByTitleContainingIgnoreCase(title);
        }
        return movieRepository.findAll();
    }

    @Override
    public Movie saveMovie(Movie movie) {
        // Here you can add business logic (e.g., validation, logging)
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    public Movie updateMovie(Movie movie) {
        movieRepository.save(movie);
        return movie;
    }

}
