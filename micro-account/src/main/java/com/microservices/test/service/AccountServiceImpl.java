package com.microservices.test.service;

import java.time.Instant;
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
import com.microservices.test.CorrelationIdFilter;
import com.microservices.test.dto.AccountDTO;
import com.microservices.test.entity.AccountEntity;
import com.microservices.test.exceptions.DuplicateResourceException;
import com.microservices.test.exceptions.ResourceNotFoundException;
import com.microservices.test.externalDto.EmployeeResponse;
import com.microservices.test.externalclient.EmployeeClient;
import com.microservices.test.repo.AccountRepo;

import jakarta.servlet.http.HttpServletRequest;
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
	

	@Autowired
	private HttpServletRequest request;
	

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
		return new ApiResponse<AccountEntity>(
				Instant.now(),
				"SUCCESS", 
				"ACCOUNT_CREATED",
				"Account details created succesfully", 
				request.getRequestURI(),
				(String) request.getAttribute(CorrelationIdFilter.CORRELATION_ID_HEADER),
				savedAccount);
	}
	@Override
	public ApiResponse<List<AccountEntity>> getAllAccounts() {
		List<AccountEntity> allSavedAccounts = accRepo.findAll();
		return new ApiResponse<List<AccountEntity>>(
				Instant.now(),
				"SUCCESS",
				"ACOOUNT_DATA_RETRIVED", 
				"Account details have been retrived successfully",
				request.getRequestURI(),
				(String) request.getAttribute(CorrelationIdFilter.CORRELATION_ID_HEADER),
				allSavedAccounts);
		
	}

	@Override
	public ApiResponse<AccountEntity> getSingleAccount(String id) {

	    AccountEntity account = accRepo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

	    return new ApiResponse<>(
	    		Instant.now(),
	            "SUCCESS",
	            "ACCOUNT_FOUND",
	            "Account data has been found successfully",
	            request.getRequestURI(),
				(String) request.getAttribute(CorrelationIdFilter.CORRELATION_ID_HEADER),
	            account
	    );
	}
	@Override
	@Transactional
	public ApiResponse<Object> deleteByEmployeeId(String id) {

	    accRepo.deleteByEmployeeId(id);

	    return new ApiResponse<>(
	    		Instant.now(),
	            "SUCCESS",
	            "ACCOUNT_DELETED",
	            "Employee account delted succesfully",
	            request.getRequestURI(),
				(String) request.getAttribute(CorrelationIdFilter.CORRELATION_ID_HEADER),
	            Collections.emptyMap()
	    );
	}

}
