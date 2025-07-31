package com.myorg.movierating.backend.resources;

import com.myorg.movierating.backend.entity.Movie;
import com.myorg.movierating.backend.entity.MovieRating;
import com.myorg.movierating.backend.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disable security filters completely
@ActiveProfiles("test")
class MovieResourceIT {

    private static final String BASE_URI = "/api/v1/movies";
    private static final String BEST_PICTURE_WINNER_PATH = "best-picture-winner";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setup() {
        movieRepository.deleteAll();
        Movie movie = new Movie();
        movie.setTitle("Titanic");
        movieRepository.save(movie);
    }

    @Test
    void testIsBestPictureWinner_True() throws Exception {
        final String title = "Gladiator";
        final Movie movie = new Movie();
        movie.setTitle(title);
        movie.setBestPictureWinner(true);
        movieRepository.save(movie);

        mockMvc.perform(get(buildUrl(BASE_URI, title, BEST_PICTURE_WINNER_PATH))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testIsBestPictureWinner_False() throws Exception {
        final String title = "Black Swan";
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setBestPictureWinner(false);
        movieRepository.save(movie);

        mockMvc.perform(get(buildUrl(BASE_URI, title, BEST_PICTURE_WINNER_PATH))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testIsBestPictureWinner_NotFound() throws Exception {
        final String title = "NonExistentTitle";
        mockMvc.perform(get(buildUrl(BASE_URI, title, BEST_PICTURE_WINNER_PATH))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testGetTop10RatedMovies() throws Exception {
        final String title = "The Kids Are All Right";
        Movie movie = Movie.builder()
                .title(title)
                .year(2010)
                .boxOffice(500000000L)
                .build();
        movie = movieRepository.save(movie);

        MovieRating rating = new MovieRating();
        rating.setMovie(movie);
        rating.setRating(9);
        rating.setUserId("user1");
        movie.setRatings(List.of(rating));
        movieRepository.save(movie);

        mockMvc.perform(get("/api/v1/movies/top-10-rated-movies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].title", is(title)));
    }

    @Test
    void testGetAllMovies() throws Exception {
        mockMvc.perform(get("/api/v1/movies/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testGetMovieById_ReturnsCorrectMovie() throws Exception {
        final String title = "True Grit";
        Movie movie = Movie.builder()
                .title(title)
                .year(1999)
                .boxOffice(460000000L)
                .build();
        movie = movieRepository.save(movie);

        mockMvc.perform(get(BASE_URI + "/" + movie.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(movie.getId().intValue())))
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.year", is(1999)))
                .andExpect(jsonPath("$.boxOffice", is(460000000)));
    }

    @Test
    void testGetMovieById_NotFound() throws Exception {
        Long nonExistentId = 99999L;

        mockMvc.perform(get(BASE_URI + "/" + nonExistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Movie not found with id: 99999")))
                .andExpect(jsonPath("$.status", is(404)));
    }

    private String buildUrl(String... pathSegments) {
        return Arrays.stream(pathSegments)
                .map(segment -> segment.replaceAll("^/+", "").replaceAll("/+$", "")) // trim slashes
                .collect(Collectors.joining("/", "/", ""));
    }

}
