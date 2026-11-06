package com.microservices.test;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T>{

	private Instant timestamp;
	private String status;
	private String code;
	private String message;
	private String path;
	private String correlationId;
	private T data;
}
