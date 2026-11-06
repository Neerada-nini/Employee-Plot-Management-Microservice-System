package com.microservices.test.exceptions;

import java.time.Instant;
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
import com.microservices.test.CorrelationIdFilter;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String getCorrelationId(HttpServletRequest request) {
        return (String) request.getAttribute(
                CorrelationIdFilter.CORRELATION_ID_HEADER);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleMalformedRequest(
            HttpMessageNotReadableException e,
            HttpServletRequest request) {
    	
    	logger.error("Unexpected error while processing request", e);

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        Instant.now(),
                        "ERROR",
                        "MALFORMED_REQUEST",
                        "Malformed or invalid request body",
                        request.getRequestURI(),
                        getCorrelationId(request),
                        Collections.emptyMap()
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        ApiResponse<Map<String, String>> apiResponse =
                new ApiResponse<>(
                        Instant.now(),
                        "ERROR",
                        "VALIDATION_ERROR",
                        "Request validation failed",
                        request.getRequestURI(),
                        getCorrelationId(request),
                        errors
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleResourceNotFoundException(
            ResourceNotFoundException e,
            HttpServletRequest request) {

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        Instant.now(),
                        "ERROR",
                        "RESOURCE_NOT_FOUND",
                       "Resource are not found",
                        request.getRequestURI(),
                        getCorrelationId(request),
                        Collections.emptyMap()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleDuplicateResourceException(
            DuplicateResourceException e,
            HttpServletRequest request) {

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        Instant.now(),
                        "ERROR",
                        "DUPLICATION_ERROR",
                        "Duplicate entry has been detected",
                        request.getRequestURI(),
                        getCorrelationId(request),
                        Collections.emptyMap()
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleFeignException(
            FeignException e,
            HttpServletRequest request) {

        logger.error("Error communicating with a downstream service", e);

        String url = e.request() != null ? e.request().url() : "";
        String employeeId = url.substring(url.lastIndexOf("/") + 1);

        HttpStatus httpStatus = HttpStatus.resolve(e.status());

        if (httpStatus == null || httpStatus.is5xxServerError()) {
            httpStatus = HttpStatus.BAD_GATEWAY;
        }

        String code;
        String message;

        if (e.status() == 404) {
            code = "DOWNSTREAM_RESOURCE_NOT_FOUND";
            message = "The requested employee was not found";
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            code = "DOWNSTREAM_SERVICE_ERROR";
            message = "Error while communicating with Employee Service";
        }

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        Instant.now(),
                        "ERROR",
                        code,
                        message,
                        request.getRequestURI(),
                        getCorrelationId(request),
                        Collections.emptyMap()
                );

        return ResponseEntity
                .status(httpStatus)
                .body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleGlobalException(
            Exception exception,
            HttpServletRequest request) {

        logger.error("Unexpected error while processing request", exception);

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        Instant.now(),
                        "ERROR",
                        "INTERNAL_SERVER_ERROR",
                        "Something went wrong",
                        request.getRequestURI(),
                        getCorrelationId(request),
                        Collections.emptyMap()
                );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiResponse);
    }
}