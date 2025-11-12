package com.example.moviesphere.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private Integer releaseYear;
    private String posterUrl;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String synopsis;
    private String director;
    private String trailerYoutubeId;
    private Integer rating;
}