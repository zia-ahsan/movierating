package com.myorg.movierating.ui.controller;

import com.myorg.movierating.backend.dto.TopRatedMovieDto;
import com.myorg.movierating.backend.service.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("removal")
@AutoConfigureMockMvc(addFilters = false) // Disables Spring Security for testing
@WebMvcTest(MovieControllerUI.class)
class MovieControllerUITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Test
    @DisplayName("GET /movies/best-picture-winner should return form view")
    void shouldShowBestPictureForm() throws Exception {
        mockMvc.perform(get("/movies/best-picture-winner"))
                .andExpect(status().isOk())
                .andExpect(view().name("best-picture-owner"));
    }

    @Test
    @DisplayName("POST /movies/best-picture-winner should return result view with attributes")
    void shouldCheckBestPictureWinner() throws Exception {
        String title = "The Godfather";
        when(movieService.isBestPictureWinner(title)).thenReturn(true);

        mockMvc.perform(post("/movies/best-picture-winner").param("title", title))
                .andExpect(status().isOk())
                .andExpect(view().name("best-picture-owner"))
                .andExpect(model().attribute("title", title))
                .andExpect(model().attribute("isWinner", true));
    }

    @Test
    @DisplayName("GET /movies/top-10-rated-movies should return top movies list")
    void shouldReturnTop10RatedMovies() throws Exception {
        List<TopRatedMovieDto> topMovies = List.of(
                new TopRatedMovieDto("Movie 1", 1999, 9.5, 100_000_000L),
                new TopRatedMovieDto("Movie 2", 2000, 9.0, 90_000_000L)
        );


        when(movieService.getTop10RatedMovies()).thenReturn(topMovies);

        mockMvc.perform(get("/movies/top-10-rated-movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("top-10-rated-movies"))
                .andExpect(model().attribute("topMovies", hasSize(2)))
                .andExpect(model().attribute("topMovies", hasItem(
                        allOf(
                                hasProperty("title", is("Movie 1")),
                                hasProperty("year", is(1999)),
                                hasProperty("averageRating", is(9.5)),
                                hasProperty("boxOffice", is(100_000_000L))
                        )
                )));

    }
}
