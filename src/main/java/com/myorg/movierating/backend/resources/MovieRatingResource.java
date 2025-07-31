package com.myorg.movierating.backend.resources;

import com.myorg.movierating.backend.dto.MovieRatingRequest;
import com.myorg.movierating.backend.entity.Movie;
import com.myorg.movierating.backend.entity.MovieRating;
import com.myorg.movierating.backend.service.MovieRatingService;
import com.myorg.movierating.backend.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@AllArgsConstructor
public class MovieRatingResource {

    private final MovieRatingService movieRatingService;
    private final MovieService movieService;

    @PostMapping
    @Operation(summary = "Submit a list of movie ratings")
    public ResponseEntity<Object> submitRatings(@RequestBody  @Valid List<MovieRatingRequest> requests, Principal principal) {
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

        return ResponseEntity.ok().build();
    }
}
