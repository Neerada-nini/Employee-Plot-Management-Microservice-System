
package com.microservices.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.microservices.test.exceptions.GlobalExceptionHandler;

class MicroAccountApplicationTests {

    @Test
    void genericExceptionReturnsSafeInternalServerError() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        RuntimeException exception =
                new RuntimeException("Sensitive internal test detail");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/account/test");

        ResponseEntity<ApiResponse<Map<String, Object>>> response =
                handler.handleGlobalException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals("ERROR", response.getBody().getStatus());
        assertEquals("Something went wrong",
                response.getBody().getMessage());
        assertEquals(Map.of(), response.getBody().getData());
        assertEquals("/api/account/test", response.getBody().getPath());

        assertEquals(false,
                response.getBody().getMessage()
                        .contains("Sensitive internal test detail"));
    }
}
