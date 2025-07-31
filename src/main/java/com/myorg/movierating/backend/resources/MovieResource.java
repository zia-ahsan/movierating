package com.myorg.movierating.backend.resources;

import com.myorg.movierating.backend.dto.TopRatedMovieDto;
import com.myorg.movierating.backend.entity.Movie;
import com.myorg.movierating.backend.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/movies")
@Tag(name = "Movies", description = "Endpoints for movie data")
@AllArgsConstructor
public class MovieResource {

    private final MovieService movieService;

    @GetMapping("/top-10-rated-movies")
    @Operation(summary = "Get top 10 movies with average user rating, sorted by box office as a tiebreaker")
    public List<TopRatedMovieDto> getTop10RatedMovies() {
        log.info("Fetching top 10 rated movies");
        return movieService.getTop10RatedMovies();
    }

    @GetMapping("/{title}/best-picture-winner")
    @Operation(summary = "Check if a movie won Best Picture")
    @ApiResponse(responseCode = "200", description = "Returns true if the movie won Best Picture")
    public boolean isBestPictureWinner(
            @Parameter(description = "Title of the movie", required = true)
            @PathVariable final String title) {
        return movieService.isBestPictureWinner(title.trim());
    }


    @GetMapping
    @Operation(summary = "Get all movies")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID")
    public Movie getMovieById(@PathVariable final Long id) {
        return movieService.getMovieById(id);
    }
}
