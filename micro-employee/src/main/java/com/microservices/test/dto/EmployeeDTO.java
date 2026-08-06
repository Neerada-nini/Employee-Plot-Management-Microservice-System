package com.microservices.test.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    @NotBlank(message = "name cannot be blank")
    @Size(min = 2, max = 30, message = "name must be between 2 to 30 characters")
    private String name;

    @NotBlank(message = "address cannot be blank")
    @Size(min = 2, max = 30, message = "address must be between 2 to 30 characters")
    private String address;

    @NotNull(message = "salary cannot be null")
    @DecimalMin(value = "1000.0", message = "salary must be at least 1000")
    @DecimalMax(value = "100000.0", message = "salary must not exceed 100000.0")
    private Double salary;
}