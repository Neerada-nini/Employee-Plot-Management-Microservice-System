package com.microservices.test.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservices.test.ApiResponse;
import com.microservices.test.dto.EmployeeDTO;
import com.microservices.test.dto.external.AccountDTO;
import com.microservices.test.dto.external.PlotDTO;
import com.microservices.test.dto.hybrid.EmployeeFullDTO;
import com.microservices.test.entity.EmployeeEntity;
import com.microservices.test.exceptions.ResourceNotFoundException;
import com.microservices.test.externalClient.AccountClient;
import com.microservices.test.externalClient.PlotClient;
import com.microservices.test.repo.EmployeeRepo;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired 
	private EmployeeRepo empRepo;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@Autowired
	private AccountClient accountClient;
	
	@Autowired
	private PlotClient plotClient;
	
	
	
	@Override
	public ApiResponse<EmployeeEntity> saveEmployee(EmployeeDTO empDTO) {
		
		EmployeeEntity emp = modelMapper.map(empDTO, EmployeeEntity.class);
		emp.setId(UUID.randomUUID().toString());
		emp.setDateTime(LocalDateTime.now().toString());
		EmployeeEntity savedEmployee = empRepo.save(emp);
		return new ApiResponse<EmployeeEntity>("SUCCESS","EMPLOYEE DATA CREATED",savedEmployee);
	}

	@Override
	public ApiResponse<List<EmployeeEntity>> getAllEmployees() {
	List<EmployeeEntity> employee=empRepo.findAll();
	
	if(employee.isEmpty()) {
		return new ApiResponse<List<EmployeeEntity>>("Success", "EMPLOYEE DATA NOT FOUND" , employee);
	} 
		return new ApiResponse<List<EmployeeEntity>>("Success", "EMPLOYE DATA FOUND" , employee);
	}

	@Override
	public ApiResponse<EmployeeEntity> getSingleEmployee(String id) {

	    EmployeeEntity employee = empRepo.findById(id)
	            .orElseThrow(() -> 
	                new ResourceNotFoundException("Employee not found with id: " + id)
	            );

	    return new ApiResponse<>(
	            "SUCCESS",
	            "EMPLOYEE FOUND",
	            employee
	    );
	}

	@Override
	public ApiResponse<EmployeeEntity> saveFullEmployee(@Valid EmployeeFullDTO empFullDTO) {

	    System.out.println("Employee Save Started");

	    EmployeeDTO employeeDTO = empFullDTO.getEmployeeDTO();

	    ApiResponse<EmployeeEntity> savedEmployeeResponse =
	            saveEmployee(employeeDTO);

	    EmployeeEntity employeeData =
	            savedEmployeeResponse.getData();

	    System.out.println("Employee Saved");

	    AccountDTO accountDTO = empFullDTO.getAccountDTO();
	    accountDTO.setEmployeeId(employeeData.getId());

	    accountClient.saveAccount(accountDTO);

	    System.out.println("Account Saved");

	    PlotDTO plotDTO = empFullDTO.getPlotDTO();
	    plotDTO.setEmployeeId(employeeData.getId());

	    plotClient.savePlot(plotDTO);

	    System.out.println("Plot Saved");

	    return new ApiResponse<>(
	            "SUCCESS",
	            "Employee full data saved",
	            employeeData
	    );
	}
	@Override
	@Transactional
	public ApiResponse<Object> deleteById(String id) {
		empRepo.deleteById(id);
		
		accountClient.deleteByEmployeeId(id);
		
		plotClient.deleteByEmployeeId(id);
		return new ApiResponse<Object>("success","employee Deleted Succesfully", Collections.emptyMap());
	}
	

}
