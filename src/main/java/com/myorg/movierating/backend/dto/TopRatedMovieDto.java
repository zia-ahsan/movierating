package com.myorg.movierating.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
public class TopRatedMovieDto {
    @NotNull
    private String title;
    private Integer year;
    private Double averageRating;
    private Long boxOffice;
}
