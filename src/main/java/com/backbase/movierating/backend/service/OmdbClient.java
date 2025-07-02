package com.backbase.movierating.backend.service;

public interface OmdbClient {
    Long fetchBoxOffice(String title, Integer year);
}
