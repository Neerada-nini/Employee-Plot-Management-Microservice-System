
package com.microservices.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.microservices.test.exceptions.GlobalExceptionHandler;

class MicroAccountApplicationTests {

    @Test
    void genericExceptionReturnsSafeInternalServerError() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        RuntimeException exception =
                new RuntimeException("Sensitive internal test detail");

        ResponseEntity<ApiResponse<Map<String, Object>>> response =
                handler.handleGlobalException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals("ERROR", response.getBody().getStatus());
        assertEquals("Something went wrong",
                response.getBody().getMessage());
        assertEquals(Map.of(), response.getBody().getData());

        assertEquals(false,
                response.getBody().getMessage()
                        .contains("Sensitive internal test detail"));
    }
}
