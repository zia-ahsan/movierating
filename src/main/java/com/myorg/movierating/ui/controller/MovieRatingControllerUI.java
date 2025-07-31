package com.myorg.movierating.ui.controller;

import com.myorg.movierating.backend.entity.Movie;
import com.myorg.movierating.backend.entity.MovieRating;
import com.myorg.movierating.backend.service.MovieRatingService;
import com.myorg.movierating.backend.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class MovieRatingControllerUI {

    private final MovieRatingService movieRatingService;
    private final MovieService movieService;
    private static final int PAGE_SIZE = 10;

    @GetMapping("/rate-movies")
    public String showRatingForm(final @RequestParam(defaultValue = "0") int page,
                                 Model model,
                                 final @AuthenticationPrincipal OidcUser oidcUser) {

        Page<Movie> moviePage = movieService.getAllMovies(PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("moviePage", moviePage);

        String userId = oidcUser.getSubject();
        Map<Long, Integer> ratings = movieRatingService.getRatingsByUser(userId);
        model.addAttribute("ratings", ratings);

        return "rate-movies";
    }

    @PostMapping("/submit-ratings")
    public String submitRatings(final HttpServletRequest request, final Authentication auth, Model model) {
        String userId = auth.getName();
        Map<String, String[]> paramMap = request.getParameterMap();
        List<MovieRating> ratingsToSave = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String key = entry.getKey();

            if (key.startsWith("ratings[") && entry.getValue().length > 0) {
                int start = key.indexOf('[') + 1;
                int end = key.indexOf(']');
                String index = key.substring(start, end);

                String ratingStr = entry.getValue()[0];
                String movieIdStr = request.getParameter("movieIds[" + index + "]");

                if (movieIdStr != null && ratingStr != null) {
                    try {
                        Long movieId = Long.parseLong(movieIdStr);
                        int rating = Integer.parseInt(ratingStr);

                        Movie movie = movieService.getMovieById(movieId);
                        ratingsToSave.add(MovieRating.builder()
                                .userId(userId)
                                .movie(movie)
                                .rating(rating)
                                .build());
                    } catch (NumberFormatException e) {
                        // Log and ignore malformed input
                    }
                }
            }
        }

        movieRatingService.saveRatings(ratingsToSave, auth);
        return "redirect:/ratings/rate-movies";
    }
}
