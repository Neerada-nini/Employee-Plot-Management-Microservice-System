package com.microservices.test.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import com.microservices.test.ApiResponse;
import com.microservices.test.dto.AccountDTO;
import com.microservices.test.entity.AccountEntity;
import com.microservices.test.exceptions.DuplicateResourceException;
import com.microservices.test.exceptions.ResourceNotFoundException;
import com.microservices.test.externalDto.EmployeeResponse;
import com.microservices.test.externalclient.EmployeeClient;
import com.microservices.test.repo.AccountRepo;

import jakarta.transaction.Transactional;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	ModelMapper modelMapper;
	

	@Autowired
	AccountRepo accRepo;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private EmployeeClient employeeClient;

	@Override
	public ApiResponse<AccountEntity> saveAccount(AccountDTO accDTO) {
	
          // 1. check if the account number already exists  
		if(accRepo.findByAccNo(accDTO.getAccNo()).isPresent()) {

	        throw new DuplicateResourceException("Account number already exists:"+accDTO.getAccNo());
	    }
		
		//2. stop default behaviour of exceptions
//		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
//			
//			public boolean hasError(ClientHttpResponse response) throws IOException{
//				return false;
//			}
//		});

        // 3. check if employee is present or not rest template method		
//		ResponseEntity<ApiResponse<EmployeeResponse>> response=restTemplate.exchange(
//				"http://localhost:8081/api/employee"+accDTO.getEmployeeId(),
//				HttpMethod.GET, 
//				null,
//				new ParameterizedTypeReference<ApiResponse<EmployeeResponse>>() {
//				
//				});
//		
//		ApiResponse<EmployeeResponse> employeeResponse = response.getBody();
//		
//		if(employeeResponse==null || !"Success".equalsIgnoreCase(employeeResponse.getStatus()) || employeeResponse.getData()==null) {
//			throw new ResourceNotFoundException("Employee not found with id" + accDTO.getEmployeeId());
//		}
		
		ResponseEntity<ApiResponse<EmployeeResponse>> singleEmployee = employeeClient.getSingleEmployee(accDTO.getEmployeeId());
		
		
		
		AccountEntity acc =modelMapper.map(accDTO, AccountEntity.class);
		acc.setId(UUID.randomUUID().toString());
		acc.setDateTime(LocalDateTime.now().toString());
		AccountEntity savedAccount = accRepo.save(acc);
		return new ApiResponse<AccountEntity>("Success", "Account details created succesfully", savedAccount);
	}
	@Override
	public ApiResponse<List<AccountEntity>> getAllAccounts() {
		List<AccountEntity> allSavedAccounts = accRepo.findAll();
		return new ApiResponse<List<AccountEntity>>("Success", "Account data retrived", allSavedAccounts);
		
	}

	@Override
	public ApiResponse<AccountEntity> getSingleAccount(String id) {

	    AccountEntity account = accRepo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

	    return new ApiResponse<>(
	            "SUCCESS",
	            "ACCOUNT FOUND",
	            account
	    );
	}
	@Override
	@Transactional
	public ApiResponse<Object> deleteByEmployeeId(String id) {

	    accRepo.deleteByEmployeeId(id);

	    return new ApiResponse<>(
	            "SUCCESS",
	            "EMPLOYEE ACCOUNT DELETED SUCCESSFULLY",
	            Collections.emptyMap()
	    );
	}

}
