
package com.microservices.test.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlotEntity {

    @Id
    private String id;

    @NotBlank(message = "Area cannot be blank")
    private String area;

    @NotBlank(message = "Coloney name cannot be blank")
    private String coloneyName;

    @NotBlank(message = "City name cannot be blank")
    private String cityName;

    @NotNull(message = "Pincode cannot be null")
    @Pattern(regexp = "\\d{6}", message = "Pincode must be 6 digits")
    private String pincode;

    private String dateTime;

    @NotBlank(message = "Employee ID cannot be blank")
    private String employeeId;

}