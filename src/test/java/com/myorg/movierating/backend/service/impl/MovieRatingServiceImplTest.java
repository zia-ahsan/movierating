package com.myorg.movierating.backend.service.impl;

import com.myorg.movierating.backend.entity.Movie;
import com.myorg.movierating.backend.entity.MovieRating;
import com.myorg.movierating.backend.repository.MovieRatingRepository;
import com.myorg.movierating.backend.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieRatingServiceImplTest {

    private MovieRatingRepository movieRatingRepository;
    private MovieService movieService;
    private MovieRatingServiceImpl movieRatingService;

    @BeforeEach
    void setUp() {
        movieRatingRepository = mock(MovieRatingRepository.class);
        movieService = mock(MovieService.class);
        movieRatingService = new MovieRatingServiceImpl(movieRatingRepository, movieService);
    }

    @Test
    void saveRatings_ShouldSaveValidRatings() {
        // Given
        Principal principal = () -> "testUser";
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        when(movieService.getMovieById(1L)).thenReturn(movie);

        MovieRating rating = new MovieRating();
        rating.setMovie(movie);
        rating.setRating(9);

        // When
        movieRatingService.saveRatings(List.of(rating), principal);

        // Then
        ArgumentCaptor<MovieRating> ratingCaptor = ArgumentCaptor.forClass(MovieRating.class);
        verify(movieRatingRepository, times(1)).save(ratingCaptor.capture());


        MovieRating savedRating = ratingCaptor.getValue();
        assertEquals(9, savedRating.getRating());
        assertEquals("testUser", savedRating.getUserId());
        assertEquals(movie, savedRating.getMovie());

    }

    @Test
    void getRatingsByUser_shouldReturnCorrectMap() {
        // GIVEN
        String userId = "test-user";

        Movie movie1 = Movie.builder().id(1L).title("Black Swan").build();
        Movie movie2 = Movie.builder().id(2L).title("The Fighter").build();

        MovieRating rating1 = MovieRating.builder()
                .movie(movie1)
                .userId(userId)
                .rating(9)
                .build();

        MovieRating rating2 = MovieRating.builder()
                .movie(movie2)
                .userId(userId)
                .rating(8)
                .build();

        when(movieRatingRepository.findByUserId(userId)).thenReturn(List.of(rating1, rating2));

        // WHEN
        Map<Long, Integer> ratingsMap = movieRatingService.getRatingsByUser(userId);

        // THEN
        assertThat(ratingsMap).hasSize(2);
        assertThat(ratingsMap).containsEntry(1L, 9);
        assertThat(ratingsMap).containsEntry(2L, 8);

        verify(movieRatingRepository).findByUserId(userId);
    }

    @Test
    void saveRatings_ShouldUpdateExistingRating() {
        // Arrange
        String userId = "testUser";
        Long movieId = 1L;
        int newRating = 4;

        Principal principal = () -> userId;

        Movie movie = new Movie();
        movie.setId(movieId);

        MovieRating inputRating = new MovieRating();
        inputRating.setRating(newRating);
        inputRating.setMovie(movie);

        MovieRating existingRating = new MovieRating();
        existingRating.setRating(2); // old rating
        existingRating.setUserId(userId);
        existingRating.setMovie(movie);

        when(movieService.getMovieById(movieId)).thenReturn(movie);
        when(movieRatingRepository.findByUserIdAndMovieId(userId, movieId))
                .thenReturn(Optional.of(existingRating));

        // Act
        movieRatingService.saveRatings(List.of(inputRating), principal);

        // Assert
        verify(movieRatingRepository, times(1)).save(existingRating);
        assertEquals(newRating, existingRating.getRating());
    }


    @Test
    void saveRatings_ShouldHandleEmptyListGracefully() {
        Principal principal = () -> "testUser";
        movieRatingService.saveRatings(List.of(), principal);
        verify(movieRatingRepository, never()).saveAll(any());
    }
}
