package com.example.moviesphere.service;

import com.example.moviesphere.dto.TmdbMovieDto;
import com.example.moviesphere.dto.TmdbSearchResponse;
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
}