package com.backbase.movierating.backend.service.impl;

import com.backbase.movierating.backend.dto.TopRatedMovieDto;
import com.backbase.movierating.backend.entity.Movie;
import com.backbase.movierating.backend.repository.MovieRepository;
import com.backbase.movierating.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    @Override
    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }


    @Cacheable("isBestPictureWinner")
    @Override
    public boolean isBestPictureWinner(String title) {
        return movieRepository.findByTitleIgnoreCase(title)
                .map(Movie::getBestPictureWinner)
                .orElse(false);
    }

    @Override
    public List<TopRatedMovieDto> getTop10RatedMovies() {
        Pageable topTen = PageRequest.of(0, 10);
        return movieRepository.findTopRatedMovies(topTen);
    }
}
