
package com.microservices.test.exceptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.microservices.test.ApiResponse;

import feign.FeignException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleMalformedRequest(
            HttpMessageNotReadableException e) {

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        "ERROR",
                        "Malformed or invalid request body",
                        Collections.emptyMap()
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiResponse<Map<String, String>> apiResponse =
                new ApiResponse<>(
                        "ERROR",
                        "Validation Error",
                        errors
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleResourceNotFoundException(
            ResourceNotFoundException e) {

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        "ERROR",
                        e.getMessage(),
                        Collections.emptyMap()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleDuplicateResourceException(
            DuplicateResourceException e) {

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        "DUPLICATE INPUT",
                        e.getMessage(),
                        Collections.emptyMap()
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleFeignException(
            FeignException e) {

        logger.error("Error communicating with a downstream service", e);

        String url = e.request() != null ? e.request().url() : "";
        String employeeId = url.substring(url.lastIndexOf("/") + 1);

        HttpStatus httpStatus = HttpStatus.resolve(e.status());

        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String message;

        if (httpStatus == HttpStatus.NOT_FOUND) {
            message = "Employee not found with id " + employeeId;
        } else {
            message = "Error while communicating with Employee Service";
        }

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        "ERROR",
                        message,
                        Collections.emptyMap()
                );

        return ResponseEntity
                .status(httpStatus)
                .body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleGlobalException(
            Exception exception) {

        logger.error("Unexpected error while processing request", exception);

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        "ERROR",
                        "Something went wrong",
                        Collections.emptyMap()
                );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiResponse);
    }
}
