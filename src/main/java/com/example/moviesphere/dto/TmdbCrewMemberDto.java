package com.example.moviesphere.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TmdbCrewMemberDto {
    private String name;
    private String job; // We look for job="Director"
}