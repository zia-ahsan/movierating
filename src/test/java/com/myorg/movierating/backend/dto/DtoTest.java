package com.myorg.movierating.backend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void testMovieRatingRequest() {
        MovieRatingRequest req = new MovieRatingRequest(1L, 8);
        assertEquals(1L, req.getMovieId());
        assertEquals(8, req.getRating());
    }

    @Test
    void testTopRatedMovieDto() {
        TopRatedMovieDto dto = new TopRatedMovieDto("Inception", 2010, 9.2, 800_000_000L);
        assertEquals("Inception", dto.getTitle());
        assertEquals(2010, dto.getYear());
        assertEquals(9.2, dto.getAverageRating());
        assertEquals(800_000_000L, dto.getBoxOffice());
    }
}
