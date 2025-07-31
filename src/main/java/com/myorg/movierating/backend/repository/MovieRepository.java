package com.myorg.movierating.backend.repository;

import com.myorg.movierating.backend.dto.TopRatedMovieDto;
import com.myorg.movierating.backend.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitleIgnoreCase(String title);

    boolean existsByTitleIgnoreCase(String title);

    @Query("SELECT new com.myorg.movierating.backend.dto.TopRatedMovieDto(" +
            "m.title, m.year, AVG(r.rating), m.boxOffice) " +
            "FROM MovieRating r " +
            "JOIN r.movie m " +
            "GROUP BY m.id, m.title, m.year, m.boxOffice " +
            "ORDER BY AVG(r.rating) DESC, m.boxOffice DESC")
    List<TopRatedMovieDto> findTopRatedMovies(Pageable pageable);
}
