package com.backbase.movierating.backend.service.impl;

import com.backbase.movierating.backend.entity.Movie;
import com.backbase.movierating.backend.repository.MovieRepository;
import com.backbase.movierating.backend.service.OmdbClient;
import com.backbase.movierating.backend.service.OscarCsvService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class OscarCsvServiceImpl implements OscarCsvService {

    private final MovieRepository movieRepository;
    private final OmdbClient omdbClient;


    @PostConstruct
    public void loadOscarWinningMoviesFromCsv() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/data/academy_awards.csv"),
                        StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Handles CSV with quotes

                if (fields.length < 5) continue;

                String rawYear = fields[0].trim();
                String category = fields[1].trim();
                String nominee = fields[2].trim();
                String won = fields[4].trim().toUpperCase();

                if ("BEST PICTURE".equalsIgnoreCase(category) && "YES".equalsIgnoreCase(won)) {
                    if (!movieRepository.existsByTitleIgnoreCase(nominee)) {
                        Movie movie = new Movie();
                        movie.setTitle(nominee);
                        movie.setBestPictureWinner(true);
                        Integer year = extractYear(rawYear);
                        movie.setYear(year);
                        Movie savedMovie = movieRepository.save(movie);
                        savedMovie.setBoxOffice(omdbClient.fetchBoxOffice(nominee, year));
                        movieRepository.save(savedMovie);
                    }
                }
            }


        } catch (Exception e) {
            throw new RuntimeException("Failed to load academy_awards.csv", e);
        }
    }

    private Integer extractYear(final String rawYear) {
        if (rawYear == null) return null;
        Matcher matcher = Pattern.compile("\\d{4}").matcher(rawYear);
        return matcher.find() ? Integer.parseInt(matcher.group()) : null;
    }
}
