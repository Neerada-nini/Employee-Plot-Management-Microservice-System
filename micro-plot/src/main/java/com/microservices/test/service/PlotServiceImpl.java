package com.microservices.test.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservices.test.ApiResponse;
import com.microservices.test.dto.PlotDTO;
import com.microservices.test.entity.PlotEntity;
import com.microservices.test.exceptions.ResourceNotFoundException;
import com.microservices.test.externalClient.EmployeeClient;
import com.microservices.test.externalClient.EmployeeResponse;
import com.microservices.test.repo.PlotRepo;

import jakarta.transaction.Transactional;

@Service
public class PlotServiceImpl implements PlotService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PlotRepo plotRepo;
	
	@Autowired
	private EmployeeClient empClient;
	
	@Override
	public ApiResponse<PlotEntity> savePlot(PlotDTO plotDto) {
		
//		check if the employeeId exists or not
		
		ResponseEntity<ApiResponse<EmployeeResponse>> singleEmployee = empClient.getSingleEmployee(plotDto.getEmployeeId());
		
		
		
		PlotEntity plot = modelMapper.map(plotDto, PlotEntity.class);
		plot.setId(UUID.randomUUID().toString());
		plot.setDateTime(LocalDateTime.now().toString());
		
		PlotEntity savedPlot=plotRepo.save(plot);
		return new ApiResponse<PlotEntity>("Success","plot Created Successfully", savedPlot);
	}

	@Override
	public ApiResponse<List<PlotEntity>> getAllPlots() {
		List<PlotEntity> plotsList=plotRepo.findAll();
		
		if(plotsList.isEmpty()) {
			return new ApiResponse<List<PlotEntity>>("Success","Plot data Not found", plotsList);
		}
		
		return new ApiResponse<List<PlotEntity>>("Success","All Plots Are Displayed Here Below", plotsList);
	}

	@Override
	public ApiResponse<PlotEntity> getSinglePlot(String id) {

	    PlotEntity singlePlot = plotRepo.findById(id)
	            .orElseThrow(() -> new  ResourceNotFoundException("Plot Not Found"));

	    return new ApiResponse<>(
	            "Success",
	            "Single Plot is displayed here",
	            singlePlot
	    );
	}

	@Override
	@Transactional
	public ApiResponse<Object> deleteByEmployeeId(String id) {
       plotRepo.deleteByEmployeeId(id);
		return  new ApiResponse<>(
	            "Success",
	            "Single Plot is displayed here",
	            Collections.emptyMap());
	}
	
	
}
