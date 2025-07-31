package com.myorg.movierating.backend.exception;

public class OmdbApiException extends RuntimeException {
    public OmdbApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
