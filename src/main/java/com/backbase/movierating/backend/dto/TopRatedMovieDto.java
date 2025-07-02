package com.backbase.movierating.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopRatedMovieDto {
    private String title;
    private int year;
    private double averageRating;
    private long boxOffice;
}
