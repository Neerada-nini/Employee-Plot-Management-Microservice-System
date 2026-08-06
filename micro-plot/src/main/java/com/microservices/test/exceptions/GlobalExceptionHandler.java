package com.microservices.test.exceptions;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.microservices.test.ApiResponse;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleGenericException(Exception e) {

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        "ERROR",
                        e.getMessage(),
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
    
    @ExceptionHandler(FeignException .class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleFeignException(
    		FeignException e) {
	  
	  String url = e.request().url();
	  System.out.println(url);
	  
	  String employeeId = url.substring(url.lastIndexOf("/")+1);
	  int status = e.status();
	  HttpStatus httpStatus = HttpStatus.resolve(status);
	  
	  if(httpStatus==null) {
		  httpStatus =   httpStatus.INTERNAL_SERVER_ERROR;
	  }
	  
	  String message;
	  if(httpStatus==httpStatus.NOT_FOUND) {
		  message="Employee not found with id "+employeeId;
	  }else {
		  message="Error while communicating with Employee Service";
	  }

        ApiResponse<Map<String, Object>> apiResponse =
                new ApiResponse<>(
                        "ERROR",
                        message,
                        Collections.emptyMap()
                );

        return new ResponseEntity<>(apiResponse,httpStatus);
    }
}