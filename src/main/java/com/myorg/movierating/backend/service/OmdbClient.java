package com.myorg.movierating.backend.service;

public interface OmdbClient {
    Long fetchBoxOffice(String title, Integer year);
}
