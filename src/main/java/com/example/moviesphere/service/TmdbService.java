package com.example.moviesphere.service;

import com.example.moviesphere.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class TmdbService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tmdb.api.base-url}")
    private String baseUrl;

    @Value("${tmdb.api.key}")
    private String apiKey;

    /**
     * Searches TMDb for movies by title.
     * @param title The title of the movie to search for.
     * @return A list of TmdbMovieDto results.
     */
    public List<TmdbMovieDto> searchMoviesByTitle(String title) {
        String url = String.format(
                "%smovie?api_key=%s&query=%s&include_adult=false",
                baseUrl + "search/",
                apiKey,
                title.replace(" ", "+") // URL encode the query
        );

        TmdbSearchResponse response = restTemplate.getForObject(url, TmdbSearchResponse.class);

        return response != null && response.getResults() != null
                ? response.getResults()
                : List.of(); // Return empty list on error
    }

    public Optional<String> getDirector(Long tmdbId) {
        // Implementation uses TmdbCreditsResponse and TmdbCrewMemberDto
        String url = String.format("%s/credits?api_key=%s", baseUrl + "movie/" + tmdbId, apiKey);

        try {
            TmdbCreditsResponse credits = restTemplate.getForObject(url, TmdbCreditsResponse.class);

            return credits.getCrew().stream()
                    .filter(c -> "Director".equalsIgnoreCase(c.getJob()))
                    .map(TmdbCrewMemberDto::getName)
                    .findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Fetches primary movie details (title, overview, poster) from TMDb by ID.
     * This uses the standard /movie/{id} endpoint.
     */
    public Optional<TmdbMovieDto> getPrimaryMovieDetails(Long tmdbId) {
        String url = String.format("%s?api_key=%s", baseUrl + "movie/" + tmdbId, apiKey);

        try {
            // TmdbMovieDto is used here to map title, overview, release_date, etc.
            TmdbMovieDto movieDto = restTemplate.getForObject(url, TmdbMovieDto.class);
            return Optional.ofNullable(movieDto);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // ------------------------------------------------------------------
    // NEW ORCHESTRATION METHOD: getFullTmdbDetails
    // ------------------------------------------------------------------
    /**
     * Orchestrates multiple API calls to build the full public detail view DTO.
     * @param tmdbId The ID from TMDb.
     * @return TmdbDetailDto containing aggregated data.
     */
    public Optional<TmdbDetailDto> getFullTmdbDetails(Long tmdbId) {
        // 1. Get primary details
        Optional<TmdbMovieDto> movieOpt = getPrimaryMovieDetails(tmdbId);
        if (movieOpt.isEmpty()) {
            return Optional.empty(); // Movie not found
        }

        TmdbMovieDto movie = movieOpt.get();

        // 2. Get additional data (director and trailer)
        Optional<String> director = getDirector(tmdbId);
        Optional<String> trailerKey = getTrailerKey(tmdbId);

        // 3. Aggregate data into the final DTO
        TmdbDetailDto detail = new TmdbDetailDto();
        detail.setTitle(movie.getTitle());
        detail.setSynopsis(movie.getOverview());

        // Extract the year from the YYYY-MM-DD date format
        detail.setReleaseYear(movie.getReleaseDate() != null && movie.getReleaseDate().length() >= 4
                ? movie.getReleaseDate().substring(0, 4)
                : "N/A");

        detail.setPosterUrl(movie.getFullPosterUrl());
        detail.setDirector(director.orElse(null));
        detail.setTrailerYoutubeId(trailerKey.orElse(null));

        return Optional.of(detail);
    }

    /**
     * Fetches the trailer key (YouTube ID) for a specific TMDb movie ID.
     * @param tmdbId The TMDb movie ID.
     * @return The YouTube key as a String, or Optional.empty().
     */
    public Optional<String> getTrailerKey(Long tmdbId) {
        String url = String.format(
                "%s/videos?api_key=%s",
                baseUrl + "movie/" + tmdbId,
                apiKey
        );

        // Use Map for complex JSON response to avoid creating a dedicated DTO just for the trailer
        @SuppressWarnings("rawtypes")
        java.util.Map response = restTemplate.getForObject(url, java.util.Map.class);

        if (response != null && response.containsKey("results")) {
            List<?> results = (List<?>) response.get("results");

            // Find the first video that is a trailer on YouTube
            return results.stream()
                    .filter(o -> o instanceof java.util.Map)
                    .map(o -> (java.util.Map) o)
                    .filter(m -> "Trailer".equalsIgnoreCase((String) m.get("type")) && "YouTube".equalsIgnoreCase((String) m.get("site")))
                    .map(m -> (String) m.get("key"))
                    .findFirst();
        }
        return Optional.empty();
    }

    public List<TmdbMovieDto> getPopularMovies() {
        String url = String.format(
                "%smovie/popular?api_key=%s",
                baseUrl,
                apiKey
        );

        TmdbSearchResponse response = restTemplate.getForObject(url, TmdbSearchResponse.class);

        return response != null && response.getResults() != null
                ? response.getResults()
                : List.of();
    }
}