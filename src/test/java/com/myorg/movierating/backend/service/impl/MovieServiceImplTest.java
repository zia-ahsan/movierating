package com.myorg.movierating.backend.service.impl;

import com.myorg.movierating.backend.dto.TopRatedMovieDto;
import com.myorg.movierating.backend.entity.Movie;
import com.myorg.movierating.backend.exception.MovieNotFoundException;
import com.myorg.movierating.backend.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void release() throws Exception {
        closeable.close();
    }

    @Test
    void testIsBestPictureWinner_True() {
        Movie movie = new Movie();
        movie.setTitle("The Godfather");
        movie.setBestPictureWinner(true);
        when(movieRepository.findByTitleIgnoreCase("The Godfather")).thenReturn(Optional.of(movie));

        boolean result = movieService.isBestPictureWinner("The Godfather");
        assertTrue(result);
    }

    @Test
    void testIsBestPictureWinner_False() {
        Movie movie = new Movie();
        movie.setTitle("Some Movie");
        movie.setBestPictureWinner(false);
        when(movieRepository.findByTitleIgnoreCase("Some Movie")).thenReturn(Optional.of(movie));

        boolean result = movieService.isBestPictureWinner("Some Movie");
        assertFalse(result);
    }

    @Test
    void testIsBestPictureWinner_NotFound() {
        when(movieRepository.findByTitleIgnoreCase("Unknown")).thenReturn(Optional.empty());
        boolean result = movieService.isBestPictureWinner("Unknown");
        assertFalse(result);
    }

    @Test
    void testGetTop10RatedMovies() {
        TopRatedMovieDto movie1 = new TopRatedMovieDto("Movie A", 1900, 9.1, 1000000L);
        TopRatedMovieDto movie2 = new TopRatedMovieDto("Movie B", 1905, 8.9, 800000L);
        List<TopRatedMovieDto> topMovies = List.of(movie1, movie2);
        Pageable topTen = PageRequest.of(0, 10);

        when(movieRepository.findTopRatedMovies(topTen)).thenReturn(topMovies);

        List<TopRatedMovieDto> result = movieService.getTop10RatedMovies();

        assertEquals(2, result.size());
        assertEquals("Movie A", result.get(0).getTitle());
        assertEquals(9.1, result.get(0).getAverageRating());
    }

    @Test
    void testGetMovieById_Found() {
        Movie movie = new Movie();
        movie.setId(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Movie result = movieService.getMovieById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetMovieById_NotFound() {
        when(movieRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieById(2L));
    }

    @Test
    void testGetAllMovies() {
        List<Movie> movies = Arrays.asList(new Movie(), new Movie());
        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> result = movieService.getAllMovies();
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllMoviesPaginated() {
        Page<Movie> moviePage = new PageImpl<>(List.of(new Movie()));
        when(movieRepository.findAll(any(PageRequest.class))).thenReturn(moviePage);

        Page<Movie> result = movieService.getAllMovies(PageRequest.of(0, 10));
        assertEquals(1, result.getContent().size());
    }
}
