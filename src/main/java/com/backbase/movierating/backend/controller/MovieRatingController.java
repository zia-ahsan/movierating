package com.backbase.movierating.backend.controller;

import com.backbase.movierating.backend.dto.MovieRatingRequest;
import com.backbase.movierating.backend.entity.Movie;
import com.backbase.movierating.backend.entity.MovieRating;
import com.backbase.movierating.backend.service.MovieRatingService;
import com.backbase.movierating.backend.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ratings")
@Tag(name = "Ratings", description = "Endpoints for submitting movie ratings")
public class MovieRatingController {

    private final MovieRatingService movieRatingService;
    private final MovieService movieService;

    @Autowired
    public MovieRatingController(MovieRatingService movieRatingService, MovieService movieService) {
        this.movieRatingService = movieRatingService;
        this.movieService = movieService;
    }

    @PostMapping("/rate-movies")
    @Operation(summary = "Submit a list of movie ratings")
    public void submitRatings(@RequestBody List<MovieRatingRequest> requests, Principal principal) {
        String userId = principal.getName();

        List<MovieRating> ratingsToSave = requests.stream().map(req -> {
            Movie movie = movieService.getMovieById(req.getMovieId());
            return MovieRating.builder()
                    .userId(userId)
                    .movie(movie)
                    .rating(req.getRating())
                    .build();
        }).collect(Collectors.toList());

        movieRatingService.saveRatings(ratingsToSave, principal);
    }
}
