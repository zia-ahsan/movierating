package com.backbase.movierating.backend.controller;

import com.backbase.movierating.backend.dto.TopRatedMovieDto;
import com.backbase.movierating.backend.entity.Movie;
import com.backbase.movierating.backend.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
@Tag(name = "Movies", description = "Endpoints for movie data")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/top-10-rated-movies")
    @Operation(summary = "Get top 10 movies with average user rating, sorted by box office as a tiebreaker")
    public List<TopRatedMovieDto> getTop10RatedMovies() {
        return movieService.getTop10RatedMovies();
    }

    @GetMapping("/is-best-picture-owner/{title}")
    @Operation(summary = "Check if a movie won Best Picture")
    @ApiResponse(responseCode = "200", description = "Returns true if the movie won Best Picture")
    public boolean isBestPictureWinner(
            @Parameter(description = "Title of the movie", required = true)
            @PathVariable String title) {
        return movieService.isBestPictureWinner(title);
    }


    @GetMapping("/all-movies")
    @Operation(summary = "Get all movies")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID")
    public Movie getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }
}
