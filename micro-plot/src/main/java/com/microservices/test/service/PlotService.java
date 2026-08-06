package com.microservices.test.service;

import java.util.List;

import com.microservices.test.ApiResponse;
import com.microservices.test.dto.PlotDTO;
import com.microservices.test.entity.PlotEntity;

public interface PlotService {

	ApiResponse<PlotEntity> savePlot(PlotDTO plotDto);

    ApiResponse<List<PlotEntity>> getAllPlots();
    
    ApiResponse<PlotEntity> getSinglePlot(String id);

	 ApiResponse<Object> deleteByEmployeeId(String id);
}
