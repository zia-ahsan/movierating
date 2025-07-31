package com.myorg.movierating.backend.service;

import com.myorg.movierating.backend.dto.TopRatedMovieDto;
import com.myorg.movierating.backend.entity.Movie;
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
