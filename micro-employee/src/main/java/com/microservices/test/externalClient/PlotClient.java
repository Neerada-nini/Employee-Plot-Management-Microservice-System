package com.microservices.test.externalClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.microservices.test.ApiResponse;
import com.microservices.test.dto.external.PlotDTO;

import jakarta.validation.Valid;


@FeignClient(name="MICRO-PLOT")
public interface PlotClient {

	@PostMapping("/api/plot")
	public ApiResponse<PlotDTO> savePlot(@Valid @RequestBody PlotDTO plotDto); 
	
	 @DeleteMapping("/api/plot/{id}")
	    public ResponseEntity<Object> deleteByEmployeeId(@PathVariable("id")String id);
}


