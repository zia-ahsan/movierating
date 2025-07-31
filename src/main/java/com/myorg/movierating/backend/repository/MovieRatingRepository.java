package com.myorg.movierating.backend.repository;

import com.myorg.movierating.backend.entity.MovieRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRatingRepository extends JpaRepository<MovieRating, Long> {
    List<MovieRating> findByUserId(String userId);
    //List<MovieRating> findByUserIdAndMovieIdIn(String userId, Set<Long> movieIds);
    Optional<MovieRating> findByUserIdAndMovieId(String userId, Long movieId);
}
