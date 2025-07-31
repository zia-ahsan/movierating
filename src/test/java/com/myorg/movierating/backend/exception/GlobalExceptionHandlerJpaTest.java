package com.myorg.movierating.backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerJpaTest {

    @Test
    void handleJpaSystemException_shouldReturnInternalServerErrorResponse() {
        RuntimeException cause = new DataAccessResourceFailureException("Database not reachable");
        JpaSystemException ex = new JpaSystemException(cause);

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ErrorResponse> response = handler.handleJpaSystemException(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Database error");
        assertThat(response.getBody().getMessage()).contains("Database not reachable");
    }

}
