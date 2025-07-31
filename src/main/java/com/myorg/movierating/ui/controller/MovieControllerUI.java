package com.myorg.movierating.ui.controller;

import com.myorg.movierating.backend.dto.TopRatedMovieDto;
import com.myorg.movierating.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieControllerUI {

    private final MovieService movieService;

    @GetMapping("/best-picture-winner")
    public String showForm() {
        return "best-picture-owner";
    }

    @PostMapping("/best-picture-winner")
    public String checkBestPictureWinner(final @RequestParam("title") String title, final Model model) {
        boolean isWinner = movieService.isBestPictureWinner(title.trim());
        model.addAttribute("title", title.trim());
        model.addAttribute("isWinner", isWinner);
        return "best-picture-owner";
    }

    @GetMapping("/top-10-rated-movies")
    public String showTopRatedMovies(final Model model) {
        List<TopRatedMovieDto> topMovies = movieService.getTop10RatedMovies();
        model.addAttribute("topMovies", topMovies);
        return "top-10-rated-movies";
    }
}
