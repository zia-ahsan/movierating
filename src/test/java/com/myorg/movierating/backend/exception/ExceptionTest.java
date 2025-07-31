package com.myorg.movierating.backend.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void testOscarDataException() {
        Exception ex = new OscarDataException("Oscar error", new RuntimeException("cause"));
        assertEquals("Oscar error", ex.getMessage());
        assertNotNull(ex.getCause());
    }

    @Test
    void testRatingNotFoundException() {
        Exception ex = new RatingNotFoundException("Rating not found");
        assertEquals("Rating not found", ex.getMessage());
    }

    @Test
    void testUnauthorizedAccessException() {
        Exception ex = new UnauthorizedAccessException("Not allowed");
        assertEquals("Not allowed", ex.getMessage());
    }

    @Test
    void testMovieNotFoundException() {
        Exception ex = new MovieNotFoundException("Movie not found");
        assertEquals("Movie not found", ex.getMessage());
    }

    @Test
    void testOmdbApiException() {
        Exception ex = new OmdbApiException("OMDb failure", new RuntimeException("root"));
        assertEquals("OMDb failure", ex.getMessage());
        assertNotNull(ex.getCause());
    }

    @Test
    void testErrorResponse() {
        ErrorResponse error = new ErrorResponse("SomeError", "Something went wrong");
        assertEquals("SomeError", error.getError());
        assertEquals("Something went wrong", error.getMessage());
    }

}
