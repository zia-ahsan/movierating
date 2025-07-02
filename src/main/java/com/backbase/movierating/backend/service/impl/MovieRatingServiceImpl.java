package com.backbase.movierating.backend.service.impl;

import com.backbase.movierating.backend.entity.Movie;
import com.backbase.movierating.backend.entity.MovieRating;
import com.backbase.movierating.backend.repository.MovieRatingRepository;
import com.backbase.movierating.backend.service.MovieRatingService;
import com.backbase.movierating.backend.service.MovieService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieRatingServiceImpl implements MovieRatingService {

    private final MovieRatingRepository movieRatingRepository;
    private final MovieService movieService;

    public MovieRatingServiceImpl(MovieRatingRepository movieRatingRepository, MovieService movieService) {
        this.movieRatingRepository = movieRatingRepository;
        this.movieService = movieService;
    }

    @Override
    @Transactional
    public void saveRatings(List<MovieRating> ratings, Principal principal) {
        String username = principal.getName();

        for (MovieRating rating : ratings) {
            Movie movie = movieService.getMovieById(rating.getMovie().getId());

            Optional<MovieRating> existingOpt = movieRatingRepository.findByUserIdAndMovieId(username, movie.getId());
            if (existingOpt.isPresent()) {
                MovieRating existing = existingOpt.get();
                existing.setRating(rating.getRating());
                movieRatingRepository.save(existing);
            } else {
                MovieRating newRating = new MovieRating();
                newRating.setUserId(username);
                newRating.setMovie(movie);
                newRating.setRating(rating.getRating());
                movieRatingRepository.save(newRating);
            }
        }
    }

    @Override
    public Map<Long, Integer> getRatingsByUser(String userId) {
        List<MovieRating> ratings = movieRatingRepository.findByUserId(userId);
        return ratings.stream()
                .collect(Collectors.toMap(
                        r -> r.getMovie().getId(),
                        MovieRating::getRating
                ));
    }


    /*@Override
    public List<MovieRating> getRatingsByUser(String username) {
        return ratingRepository.findByUserId(username);
    }*/

    /*@Override
    public MovieRating getRatingByUserAndMovie(String username, Long movieId) {
        return ratingRepository.findByUserIdAndMovieId(username, movieId);
    }*/
}
