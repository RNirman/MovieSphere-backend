package com.example.moviesphere.controller;

import com.example.moviesphere.dto.TmdbDetailDto;
import com.example.moviesphere.model.Movie;
import com.example.moviesphere.repository.MovieRepository;
import com.example.moviesphere.service.MovieService;
import com.example.moviesphere.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.moviesphere.service.TmdbService;
import com.example.moviesphere.dto.TmdbMovieDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.moviesphere.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;
    private final TmdbService tmdbService;

    @Autowired
    public MovieController(MovieService movieService, TmdbService tmdbService) {
        this.movieService = movieService;
        this.tmdbService = tmdbService;
    }

    @GetMapping("/auth/verify")
    public ResponseEntity<Map<String, String>> verifyAuth() {
        return ResponseEntity.ok(Map.of("status", "authenticated"));
    }

    // GET: List all movies
    @GetMapping
    public List<Movie> getAllMovies(@RequestParam(required = false) String title) {
        // Call the new service method that handles filtering
        return movieService.searchMovies(title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.findMovieById(id) // Assuming you implemented findMovieById
                .orElseThrow(() -> new RuntimeException("Movie not found for ID: " + id));

        return ResponseEntity.ok(movie);
    }

    @GetMapping("/public/search")
    public List<TmdbMovieDto> publicSearchTmdb(@RequestParam String title) {
        // This calls the same TmdbService as the admin panel,
        // but the endpoint is designated as public in SecurityConfig.
        return tmdbService.searchMoviesByTitle(title);
    }

    // POST: Create a new movie
    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.saveMovie(movie); // Delegate to the service
    }

    // DELETE: Delete a movie
    @DeleteMapping("/{id}") // The path must include the /{id} placeholder
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movieDetails) {
        Movie existingMovie = movieService.findMovieById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found for ID: " + id));

        existingMovie.setTitle(movieDetails.getTitle());
        existingMovie.setGenre(movieDetails.getGenre());
        existingMovie.setReleaseYear(movieDetails.getReleaseYear());
        existingMovie.setPosterUrl(movieDetails.getPosterUrl());

        Movie updatedMovie = movieService.saveMovie(existingMovie);

        return ResponseEntity.ok(updatedMovie);
    }

    @GetMapping("/search/tmdb") // e.g., /api/v1/movies/search/tmdb?title=Inception
    public List<TmdbMovieDto> searchTmdb(@RequestParam String title) {
        return tmdbService.searchMoviesByTitle(title);
    }

    @GetMapping("/public/tmdb/details/{id}")
    public TmdbDetailDto getFullTmdbDetails(@PathVariable Long id) {
        // Use the orchestration method from the service layer
        return tmdbService.getFullTmdbDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("TMDb details not found for ID: " + id));
    }

}