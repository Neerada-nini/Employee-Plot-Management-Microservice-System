
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
import com.microservices.test.dto.PlotDTO;
import com.microservices.test.entity.PlotEntity;
import com.microservices.test.service.PlotService;

import jakarta.validation.Valid;

@RequestMapping("/api/plot")
@RestController
public class PlotMainController {

    @Autowired
    private PlotService plotService;

    @PostMapping
    public ResponseEntity<ApiResponse<PlotEntity>> savePlot(
            @Valid @RequestBody PlotDTO plotDTO) {

        ApiResponse<PlotEntity> savedPlotResponse =
                plotService.savePlot(plotDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPlotResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlotEntity>>> getAllPlots() {

        ApiResponse<List<PlotEntity>> allPlots =
                plotService.getAllPlots();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allPlots);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlotEntity>> getSinglePlot(
            @PathVariable("id") String id) {

        ApiResponse<PlotEntity> singlePlotResponse =
                plotService.getSinglePlot(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(singlePlotResponse);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteByEmployeeId(
            @PathVariable("id") String id) {

        ApiResponse<Object> response = plotService.deleteByEmployeeId(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
