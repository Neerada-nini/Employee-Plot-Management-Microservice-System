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

import com.microservices.test.ApiResponse;
import com.microservices.test.CorrelationIdFilter;
import com.microservices.test.dto.PlotDTO;
import com.microservices.test.entity.PlotEntity;
import com.microservices.test.exceptions.ResourceNotFoundException;
import com.microservices.test.externalClient.EmployeeClient;
import com.microservices.test.externalClient.EmployeeResponse;
import com.microservices.test.repo.PlotRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class PlotServiceImpl implements PlotService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PlotRepo plotRepo;
	
	@Autowired
	private EmployeeClient empClient;
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public ApiResponse<PlotEntity> savePlot(PlotDTO plotDto) {
		
//		check if the employeeId exists or not
		
		ResponseEntity<ApiResponse<EmployeeResponse>> singleEmployee = empClient.getSingleEmployee(plotDto.getEmployeeId());
		
		
		
		PlotEntity plot = modelMapper.map(plotDto, PlotEntity.class);
		plot.setId(UUID.randomUUID().toString());
		plot.setDateTime(LocalDateTime.now().toString());
		
		PlotEntity savedPlot=plotRepo.save(plot);
		return new ApiResponse<PlotEntity>(
				Instant.now(),
				"SUCCESS", 
				"PLOT_CREATED",
				"plot Created Successfully", 
				request.getRequestURI(),
				(String) request.getAttribute(CorrelationIdFilter.CORRELATION_ID_HEADER),
				savedPlot);
	}

	@Override
	public ApiResponse<List<PlotEntity>> getAllPlots() {
		List<PlotEntity> plotsList=plotRepo.findAll();
		
		System.out.println("REQUEST URI = " + request.getRequestURI());

		System.out.println("CORRELATION ID = " +
		    request.getAttribute(CorrelationIdFilter.CORRELATION_ID_HEADER));
		
		if(plotsList.isEmpty()) {
			return new ApiResponse<List<PlotEntity>>(
					Instant.now(),
					"SUCCESS",
					"PLOT_DATA_NOT_RETRIVED", 
					"Pot details have not been found", 
					request.getRequestURI(),
					(String) request.getAttribute(CorrelationIdFilter.CORRELATION_ID_HEADER),
					plotsList);
		}
		
		return new ApiResponse<List<PlotEntity>>(
				Instant.now(),
				"SUCCESS",
				"PLOT_DATA_RETRIVED", 
				"Pot details have been retrived successfully", 
				request.getRequestURI(),
				(String) request.getAttribute(CorrelationIdFilter.CORRELATION_ID_HEADER),
				plotsList);
	}

	@Override
	public ApiResponse<PlotEntity> getSinglePlot(String id) {

	    PlotEntity singlePlot = plotRepo.findById(id)
	            .orElseThrow(() -> new  ResourceNotFoundException("Plot Not Found"));

	    return new ApiResponse<>(
	    		Instant.now(),
	    		"SUCCESS",
	    		"PLOT_DETAILS__RETRIVED", 
	            "Single Plot is displayed here",
	            request.getRequestURI(),
				(String) request.getAttribute(CorrelationIdFilter.CORRELATION_ID_HEADER), 
	            singlePlot);
	}

	@Override
	@Transactional
	public ApiResponse<Object> deleteByEmployeeId(String id) {
       plotRepo.deleteByEmployeeId(id);
		return  new ApiResponse<>(
				Instant.now(),
	            "SUCCESS",
	            "PLOT_DELETED",
	            "Employee Plot have been deleted succesfully",
	            request.getRequestURI(),
				(String) request.getAttribute(CorrelationIdFilter.CORRELATION_ID_HEADER),
	            Collections.emptyMap());
	}
	
	
}
