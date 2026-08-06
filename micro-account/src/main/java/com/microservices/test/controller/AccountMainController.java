package com.microservices.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.test.ApiResponse;
import com.microservices.test.dto.AccountDTO;
import com.microservices.test.entity.AccountEntity;
import com.microservices.test.service.AccountService;

import jakarta.validation.Valid;

@RequestMapping("/api/account")
@RestController
 
public class AccountMainController {
	
	@Autowired
	private AccountService accService;

	@PostMapping
	public ResponseEntity<ApiResponse<AccountEntity>> saveAccounts( @Valid @RequestBody AccountDTO accDto) {
	
		ApiResponse<AccountEntity> savedAccount= accService.saveAccount(accDto);
		return  ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<AccountEntity>>> getAllSavedAccounts() {
	
		ApiResponse<List<AccountEntity>> savedAccount= accService.getAllAccounts();
		return  ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<AccountEntity>> getSingleAccount(@PathVariable("id") String Id) {
	
		ApiResponse<AccountEntity> savedSingleAccount= accService.getSingleAccount(Id);
		return  ResponseEntity.status(HttpStatus.FOUND).body(savedSingleAccount);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> deleteByEmployeeId(@PathVariable("id") String Id) {
	
		ApiResponse<Object>  deleteByEmployeeId =  accService. deleteByEmployeeId((Id));
		return  ResponseEntity.status(HttpStatus.OK).body(deleteByEmployeeId);
	}
	
	
}
