package com.backbase.movierating.backend.projection;

public interface TopRatedMovieView {
    String getTitle();
    Integer getYear();
    Double getAverageRating();
    Long getBoxOffice();
}
