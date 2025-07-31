package com.myorg.movierating.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieRatingRequest {

    @NotNull(message = "movieId is required")
    private Long movieId;
    @NotNull(message = "rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must not exceed 10")
    private Integer rating;
}