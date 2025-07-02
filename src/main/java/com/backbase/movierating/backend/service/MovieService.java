package com.backbase.movierating.backend.service;

import com.backbase.movierating.backend.dto.TopRatedMovieDto;
import com.backbase.movierating.backend.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    Movie getMovieById(Long id);
    Page<Movie> getAllMovies(Pageable pageable);
    List<Movie> getAllMovies();
    boolean isBestPictureWinner(String title);
    List<TopRatedMovieDto> getTop10RatedMovies();
}
