package com.backbase.movierating.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_ratings",
       uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "movie_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
}
