package com.myorg.movierating.backend.exception;

import com.myorg.movierating.backend.resources.TestExceptionThrowingResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestExceptionThrowingResource.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testOscarDataException() throws Exception {
        mockMvc.perform(get("/test-ex/oscar"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Oscar error"));
    }

    @Test
    void testRatingNotFoundException() throws Exception {
        mockMvc.perform(get("/test-ex/rating"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Rating missing"));
    }

    @Test
    void testUnauthorizedAccessException() throws Exception {
        mockMvc.perform(get("/test-ex/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("No access"));
    }

    @Test
    void testMovieNotFoundException() throws Exception {
        mockMvc.perform(get("/test-ex/movie"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }

    @Test
    void testOmdbApiException() throws Exception {
        mockMvc.perform(get("/test-ex/omdb"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.message").value("OMDb error"));
    }

    @Test
    void testGenericException() throws Exception {
        mockMvc.perform(get("/test-ex/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal server error"));
    }
}

