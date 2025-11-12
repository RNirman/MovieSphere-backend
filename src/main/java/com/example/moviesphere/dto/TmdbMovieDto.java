package com.example.moviesphere.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

// Represents the fields we want from a single TMDb search result
@Data
@NoArgsConstructor
public class TmdbMovieDto {
    private Long id; // TMDb ID, useful for later calls (e.g., trailer search)
    private String title;
    private String overview; // This will be our synopsis

    @JsonProperty("release_date")
    private String releaseDate; // Need to parse this to get the year

    @JsonProperty("poster_path")
    private String posterPath;

    // TMDb uses a base image URL, which we'll prepend to posterPath
    public String getFullPosterUrl() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }
}