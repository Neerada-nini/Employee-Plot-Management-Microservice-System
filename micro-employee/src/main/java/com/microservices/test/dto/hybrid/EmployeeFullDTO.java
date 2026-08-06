package com.microservices.test.dto.hybrid;

import com.microservices.test.dto.EmployeeDTO;
import com.microservices.test.dto.external.AccountDTO;
import com.microservices.test.dto.external.PlotDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeFullDTO {
	
    @Valid
    @NotNull
	private EmployeeDTO employeeDTO;
	
    @Valid
    @NotNull
	private PlotDTO plotDTO;
	
    @Valid
    @NotNull
	private AccountDTO accountDTO;
}
