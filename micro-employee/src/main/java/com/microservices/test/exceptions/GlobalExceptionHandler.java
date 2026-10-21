package com.microservices.test.exceptions;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import com.microservices.test.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
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
	
	
	private static final Logger logger =
	        LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleGenericException(Exception e) {

    	logger.error("Unexpected error while processing request", e);
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


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError fr : e.getBindingResult().getFieldErrors()) {
            errors.put(fr.getField(), fr.getDefaultMessage());
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
}