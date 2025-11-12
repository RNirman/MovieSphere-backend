package com.example.moviesphere.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// Represents the full JSON response from the /search/movie endpoint
@Data
@NoArgsConstructor
public class TmdbSearchResponse {
    private List<TmdbMovieDto> results;
    private Integer totalPages;
    private Long totalResults;
}