package com.myorg.movierating.backend.service.impl;

import com.myorg.movierating.backend.entity.Movie;
import com.myorg.movierating.backend.exception.OscarDataException;
import com.myorg.movierating.backend.repository.MovieRepository;
import com.myorg.movierating.backend.service.OmdbClient;
import com.myorg.movierating.backend.service.OscarCsvService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Profile("!test")
@Service
@AllArgsConstructor
@Slf4j
public class OscarCsvServiceImpl implements OscarCsvService {

    private final MovieRepository movieRepository;
    private final OmdbClient omdbClient;
    private static final String WINNER = "YES";

    @PostConstruct
    public void loadBestPictureNomineesFromCsv() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/data/academy_awards.csv")),
                        StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Handles CSV with quotes

                if (fields.length < 5) continue;

                String rawYear = fields[0].trim();
                String category = fields[1].trim();
                String nominee = fields[2].trim();
                Boolean won = fields[4].trim().equalsIgnoreCase(WINNER);

                if ("BEST PICTURE".equalsIgnoreCase(category)) {
                    if (!movieRepository.existsByTitleIgnoreCase(nominee)) {
                        Movie movie = new Movie();
                        movie.setTitle(nominee);
                        movie.setBestPictureWinner(won);
                        Integer year = extractYear(rawYear);
                        movie.setYear(year);
                        Movie savedMovie = movieRepository.save(movie);
                        savedMovie.setBoxOffice(omdbClient.fetchBoxOffice(nominee, year));
                        movieRepository.save(savedMovie);
                    }
                }
            }


        } catch (IOException e) {
            log.error("Error reading Oscar CSV file", e);
            throw new OscarDataException("Failed to load Oscar data", e);
        }
    }

    private Integer extractYear(final String rawYear) {
        if (rawYear == null) return null;
        Matcher matcher = Pattern.compile("\\d{4}").matcher(rawYear);
        return matcher.find() ? Integer.parseInt(matcher.group()) : null;
    }

}
