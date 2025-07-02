package com.backbase.movierating.backend.dto;

import lombok.Data;

@Data
public class MovieRatingRequest {
    private Long movieId;
    private Integer rating;
}