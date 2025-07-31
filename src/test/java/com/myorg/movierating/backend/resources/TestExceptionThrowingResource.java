package com.myorg.movierating.backend.resources;

import com.myorg.movierating.backend.exception.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-ex")
public class TestExceptionThrowingResource {

    @GetMapping("/rating")
    public void throwRating() {
        throw new RatingNotFoundException("Rating missing");
    }

    @GetMapping("/unauthorized")
    public void throwUnauthorized() {
        throw new UnauthorizedAccessException("No access");
    }

    @GetMapping("/oscar")
    public void throwOscar() {
        throw new OscarDataException("Oscar error", new RuntimeException("source"));
    }

    @GetMapping("/omdb")
    public void throwOmdb() {
        throw new OmdbApiException("OMDb error", new RuntimeException("api"));
    }

    @GetMapping("/movie")
    public void throwMovie() {
        throw new MovieNotFoundException("Movie not found");
    }

    @GetMapping("/generic")
    public void throwGeneric() {
        throw new RuntimeException("Boom");
    }
}
