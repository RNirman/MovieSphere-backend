package com.example.moviesphere.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TmdbDetailDto {
    private String title;
    private String synopsis;
    private String releaseYear;
    private String posterUrl;
    private String director;
    private String trailerYoutubeId;
}