
package com.microservices.test.externalClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.microservices.test.ApiResponse;
import com.microservices.test.dto.external.AccountDTO;

import jakarta.validation.Valid;


@FeignClient(name="MICRO-ACCOUNT")
public interface AccountClient {

	@PostMapping("/api/account")
	public ApiResponse<AccountDTO> saveAccount(@Valid @RequestBody AccountDTO accDTO);
		
	@DeleteMapping("/api/account/{id}")
	public ResponseEntity<ApiResponse<Object>> deleteByEmployeeId(@PathVariable("id") String Id);
}
