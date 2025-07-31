package com.myorg.movierating.backend.resources;

import com.myorg.movierating.backend.dto.MovieRatingRequest;
import com.myorg.movierating.backend.entity.Movie;
import com.myorg.movierating.backend.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disables Spring Security for testing
@ActiveProfiles("test")
class MovieRatingResourceIT {

    private static final String BASE_URL = "/api/v1/ratings";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long movieId;

    @BeforeEach
    void setup() {
        movieRepository.deleteAll();

        Movie movie = Movie.builder()
                .title("The Social Network")
                .year(2010)
                .boxOffice(224000000L)
                .build();

        movie = movieRepository.save(movie);
        movieId = movie.getId();
    }

    @Test
    void submitRatings_shouldReturnOk() throws Exception {

        Principal mockPrincipal = () -> "test-user";

        mockMvc.perform(post("/api/v1/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(new MovieRatingRequest(movieId, 9))))
                        .principal(mockPrincipal))
                .andExpect(status().isOk());
    }


    @Test
    void submitRatings_shouldFailMinValidation() throws Exception {

        Principal mockPrincipal = () -> "test-user";

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(new MovieRatingRequest(movieId, -9))))
                        .principal(mockPrincipal))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Rating must be at least 1")));
    }

    @Test
    void submitRatings_shouldFailMaxValidation() throws Exception {

        Principal mockPrincipal = () -> "test-user";

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(new MovieRatingRequest(movieId, 91))))
                        .principal(mockPrincipal))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Rating must not exceed 10")));
    }
}
