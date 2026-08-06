package com.microservices.test.service;

import java.util.List;

import com.microservices.test.ApiResponse;
import com.microservices.test.dto.EmployeeDTO;
import com.microservices.test.dto.hybrid.EmployeeFullDTO;
import com.microservices.test.entity.EmployeeEntity;

import jakarta.validation.Valid;


public interface EmployeeService {

	ApiResponse<EmployeeEntity> saveEmployee(EmployeeDTO empDTO);	
	
	ApiResponse<List<EmployeeEntity>> getAllEmployees();
	
	ApiResponse<EmployeeEntity> getSingleEmployee(String id);

	ApiResponse<EmployeeEntity> saveFullEmployee(@Valid EmployeeFullDTO empFullDTO);

	ApiResponse<Object> deleteById(String id);
	}
	

