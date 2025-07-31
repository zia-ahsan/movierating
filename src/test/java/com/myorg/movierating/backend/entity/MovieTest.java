package com.myorg.movierating.backend.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void testConstructorAndGetters() {
        Movie movie = new Movie(1L, "Inception", 2010, 800_000_000L, true, null);
        assertEquals(1L, movie.getId());
        assertEquals("Inception", movie.getTitle());
        assertEquals(2010, movie.getYear());
        assertEquals(800_000_000L, movie.getBoxOffice());
        assertTrue(movie.getBestPictureWinner());
        assertNull(movie.getRatings());
    }

    @Test
    void testSetters() {
        Movie movie = new Movie();
        movie.setId(2L);
        movie.setTitle("Interstellar");
        movie.setYear(2014);
        movie.setBoxOffice(700_000_000L);
        movie.setBestPictureWinner(false);

        assertEquals("Interstellar", movie.getTitle());
        assertEquals(2014, movie.getYear());
    }

    @Test
    void testEqualsAndHashCode() {
        Movie m1 = Movie.builder().id(1L).title("A").year(2020).build();
        Movie m2 = Movie.builder().id(1L).title("A").year(2020).build();
        Movie m3 = Movie.builder().id(2L).title("B").year(2021).build();

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertEquals(m1.hashCode(), m2.hashCode());
        assertNotEquals(m1.hashCode(), m3.hashCode());
    }

    @Test
    void testToString() {
        Movie movie = Movie.builder().id(10L).title("Matrix").build();
        String str = movie.toString();
        assertTrue(str.contains("Matrix"));
        assertTrue(str.contains("id=10"));
    }

    @Test
    void testMovieBuilder() {
        Movie movie = Movie.builder()
                .id(1L)
                .title("Inception")
                .year(2010)
                .boxOffice(825_532_764L)
                .bestPictureWinner(true)
                .ratings(List.of())
                .build();

        assertEquals("Inception", movie.getTitle());
        assertEquals(2010, movie.getYear());
        assertEquals(825_532_764L, movie.getBoxOffice());
        assertTrue(movie.getBestPictureWinner());
        assertNotNull(movie.getRatings());
    }

    @Test
    void testMovieBuilderToString() {
        Movie.MovieBuilder builder = Movie.builder()
                .id(1L)
                .title("Builder Movie")
                .year(2022)
                .boxOffice(500_000_000L)
                .bestPictureWinner(true)
                .ratings(List.of());

        String builderStr = builder.toString();
        assertNotNull(builderStr);
        assertTrue(builderStr.contains("Builder Movie"));
        assertTrue(builderStr.contains("2022"));
    }


}
