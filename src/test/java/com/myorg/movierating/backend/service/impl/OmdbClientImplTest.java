package com.myorg.movierating.backend.service.impl;

import com.myorg.movierating.backend.exception.OmdbApiException;
import com.myorg.movierating.backend.service.impl.OmdbClientImpl.OmdbResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OmdbClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OmdbClientImpl omdbClient;

    private final String apiKey = "dummy-api-key";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(omdbClient, "apiKey", apiKey);
    }

    @Test
    void fetchBoxOffice_successfulResponse_returnsParsedBoxOffice() {
        OmdbResponse mockResponse = new OmdbResponse();
        mockResponse.setBoxOffice("$123,456,789");

        when(restTemplate.getForObject(anyString(), eq(OmdbResponse.class)))
                .thenReturn(mockResponse);

        Long result = omdbClient.fetchBoxOffice("Inception", 2010);

        assertEquals(123456789L, result);
    }

    @Test
    void fetchBoxOffice_boxOfficeN_A_returnsZero() {
        OmdbResponse mockResponse = new OmdbResponse();
        mockResponse.setBoxOffice("N/A");

        when(restTemplate.getForObject(anyString(), eq(OmdbResponse.class)))
                .thenReturn(mockResponse);

        Long result = omdbClient.fetchBoxOffice("Unknown Movie", 2020);

        assertEquals(0L, result);
    }

    @Test
    void fetchBoxOffice_nullResponse_returnsZero() {
        when(restTemplate.getForObject(anyString(), eq(OmdbResponse.class)))
                .thenReturn(null);

        Long result = omdbClient.fetchBoxOffice("Null Movie", 2020);

        assertEquals(0L, result);
    }

    @Test
    void fetchBoxOffice_apiThrowsException_throwsOmdbApiException() {
        when(restTemplate.getForObject(anyString(), eq(OmdbResponse.class)))
                .thenThrow(new RuntimeException("API down"));

        OmdbApiException exception = assertThrows(OmdbApiException.class, () ->
                omdbClient.fetchBoxOffice("Crashed Movie", 1999));

        assertTrue(exception.getMessage().contains("Failed to fetch data from OMDB"));
    }
}
