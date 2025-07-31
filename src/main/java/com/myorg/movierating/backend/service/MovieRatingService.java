package com.myorg.movierating.backend.service;

import com.myorg.movierating.backend.entity.MovieRating;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface MovieRatingService {

    void saveRatings(List<MovieRating> ratings, Principal principal);

    Map<Long, Integer> getRatingsByUser(String userId);

    // List<MovieRating> getRatingsByUser(String username);

    // MovieRating getRatingByUserAndMovie(String username, Long movieId);
}
