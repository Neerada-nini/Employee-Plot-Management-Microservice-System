package com.microservices.test.exceptions;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.microservices.test.ApiResponse;

import feign.FeignException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Map<String, Object>>> handleGlobalException(Exception exception) {
		 ApiResponse<Map<String, Object>> apiResponse =new ApiResponse<>(
				 "ERROR",
				 exception.getMessage(),
				 Collections.emptyMap()
				 );
		 
		 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, Object>>> handleMethoArgumentNotValidException(MethodArgumentNotValidException exception) {
		 ApiResponse<Map<String, Object>> apiResponse =new ApiResponse<>(
				 "ERROR",
				 "VALIDATION ERROR",
				 Collections.emptyMap()
				 );
		 
		 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
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
		  

		  System.out.println("********************************");
	        ApiResponse<Map<String, Object>> apiResponse =
	                new ApiResponse<>(
	                        "ERROR",
	                        message,
	                        Collections.emptyMap()
	                );

	        return new ResponseEntity<>(apiResponse,httpStatus);
	    }
	}

