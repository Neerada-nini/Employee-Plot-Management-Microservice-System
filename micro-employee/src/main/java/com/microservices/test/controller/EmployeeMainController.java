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
import com.microservices.test.dto.EmployeeDTO;
import com.microservices.test.dto.hybrid.EmployeeFullDTO;
import com.microservices.test.entity.EmployeeEntity;
import com.microservices.test.service.EmployeeService;

import jakarta.validation.Valid;

@RequestMapping("/api/employee")
@RestController
public class EmployeeMainController {

	@Autowired
	private EmployeeService empService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<EmployeeEntity>>  saveEmployee(@Valid @RequestBody EmployeeDTO empDTO) {
		
		ApiResponse<EmployeeEntity> savedEmployeeResponse = empService.saveEmployee(empDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployeeResponse);
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<EmployeeEntity>>> getAllEmployee(){
	
   ApiResponse<List<EmployeeEntity>> getAllEmployee= empService.getAllEmployees();
		
   return ResponseEntity.ok(getAllEmployee);
		
	}
	
	@GetMapping("/{id}") 
	public ResponseEntity<ApiResponse<EmployeeEntity>> getSingleEmployee(@PathVariable("id") String id){
		
		ApiResponse<EmployeeEntity> savedSingleEmployeeResponse = empService.getSingleEmployee(id);
		return ResponseEntity.ok(savedSingleEmployeeResponse);
	}
	
	
	@PostMapping("/full")
	public ResponseEntity<ApiResponse<EmployeeEntity>> saveFullEmployee(@Valid @RequestBody EmployeeFullDTO empFullDTO ){
		
		ApiResponse<EmployeeEntity> savedEmployeeFullResponse = empService.saveFullEmployee(empFullDTO );
		return ResponseEntity.ok(savedEmployeeFullResponse);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> deleteEmployee(@PathVariable("id") String id){
		
		ApiResponse<Object> response = empService.deleteById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
		
	}
	