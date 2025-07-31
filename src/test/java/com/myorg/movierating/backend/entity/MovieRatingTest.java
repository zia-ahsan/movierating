package com.myorg.movierating.backend.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovieRatingTest {
    @Test
    void testMovieRatingBuilder() {
        Movie dummyMovie = Movie.builder()
                .id(1L)
                .title("Dummy")
                .year(2000)
                .build();

        MovieRating rating = MovieRating.builder()
                .id(100L)
                .userId("user@example.com")
                .rating(9)
                .movie(dummyMovie)
                .build();

        assertEquals(100L, rating.getId());
        assertEquals("user@example.com", rating.getUserId());
        assertEquals(9, rating.getRating());
        assertEquals(dummyMovie, rating.getMovie());
    }

    @Test
    void testMovieRatingBuilderToString() {
        Movie dummy = Movie.builder().id(5L).title("Dummy").build();

        MovieRating.MovieRatingBuilder builder = MovieRating.builder()
                .id(99L)
                .userId("user@builder.com")
                .rating(7)
                .movie(dummy);

        String builderStr = builder.toString();
        assertNotNull(builderStr);
        assertTrue(builderStr.contains("user@builder.com"));
        assertTrue(builderStr.contains("rating=7"));
    }
}
