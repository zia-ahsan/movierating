package com.myorg.movierating.backend.service.impl;

import com.myorg.movierating.backend.entity.Movie;
import com.myorg.movierating.backend.repository.MovieRepository;
import com.myorg.movierating.backend.service.OmdbClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OscarCsvServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private OmdbClient omdbClient;

    @InjectMocks
    private OscarCsvServiceImpl oscarCsvService;

    @BeforeEach
    void setup() {
        when(movieRepository.existsByTitleIgnoreCase(anyString())).thenReturn(false);
        when(movieRepository.save(any(Movie.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(omdbClient.fetchBoxOffice(anyString(), anyInt())).thenReturn(123456L);
    }

    @Test
    void test_loadBestPictureNomineesFromCsv_fromTestResource() throws Exception {
        // Point resource loader to test CSV by putting file in expected path
        InputStream testCsv = getClass().getResourceAsStream("/data/academy_awards.csv");
        assert testCsv != null;

        // Act
        oscarCsvService.loadBestPictureNomineesFromCsv();

        // Assert that save was called exactly 5 times (for 5 best picture nominees in the test file)
        ArgumentCaptor<Movie> captor = ArgumentCaptor.forClass(Movie.class);
        verify(movieRepository, times(10)).save(captor.capture()); // once before and once after setting box office

        // Each nominee is saved twice: initial save + save with box office, so expect 10 calls
        List<Movie> allSaved = captor.getAllValues();
        assertThat(allSaved).hasSize(10);

        // Extract just the first-time saves (before box office set)
        List<Movie> firstPassSaves = captor.getAllValues().stream()
                .filter(m -> m.getBoxOffice() == null) // only before omdbClient.setBoxOffice
                .toList();

        // Verify titles
        List<String> expectedTitles = List.of(
                "The Blind Side",
                "District 9",
                "An Education",
                "The Hurt Locker",
                "Inglourious Basterds"
        );

        // Extract distinct movie titles from the captured saves
        List<String> actualTitles = allSaved.stream()
                .map(Movie::getTitle)
                .distinct()
                .collect(Collectors.toList());

        // Assert the exact 5 expected titles were saved
        assertThat(actualTitles)
                .containsExactlyInAnyOrderElementsOf(expectedTitles);

    }

}
