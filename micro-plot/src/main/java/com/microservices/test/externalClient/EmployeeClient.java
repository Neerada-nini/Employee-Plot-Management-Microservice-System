package com.microservices.test.externalClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.microservices.test.ApiResponse;


@FeignClient(name="MICRO-EMPLOYEE")
public interface EmployeeClient {

	@GetMapping("/api/employee/{id}")
	public ResponseEntity<ApiResponse<EmployeeResponse>> getSingleEmployee(@PathVariable("id") String id);
}
