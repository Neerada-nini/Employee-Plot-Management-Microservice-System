package com.microservices.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlotDTO {
	 @NotBlank(message = "Area cannot be blank")
	    private String area;

	    @NotBlank(message = "Coloney name cannot be blank")
	    private String coloneyName;

	    @NotBlank(message = "City name cannot be blank")
	    private String cityName;

	   
	    @NotBlank(message = "Pincode cannot be blank")
	    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be exactly 6 digits")
	    private String pincode;

	    @NotBlank(message = "Employee ID cannot be blank")
	    private String employeeId;
}
