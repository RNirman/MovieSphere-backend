package com.example.moviesphere.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class TmdbCreditsResponse {
    private List<TmdbCrewMemberDto> crew;
}