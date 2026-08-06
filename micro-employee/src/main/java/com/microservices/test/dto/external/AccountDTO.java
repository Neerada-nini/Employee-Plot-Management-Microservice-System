package com.microservices.test.dto.external;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

	 @NotBlank(message = "Account number cannot be blank")
	    @Pattern(
	        regexp = "^[0-9]{10,18}$",
	        message = "Account number must contain only digits and be between 10 to 18 digits"
	    )
	    private String accNo;

	    @NotBlank(message = "Bank number cannot be blank")
	    @Pattern(
	        regexp = "^[0-9]{6}$",
	        message = "Bank number must contain exactly 6 digits"
	    )
	    private String bankNo;

	    @NotBlank(message = "IFSC code cannot be blank")
	    @Pattern(
	        regexp = "^[A-Z]{4}0[A-Z0-9]{6}$",
	        message = "Invalid IFSC code format"
	    )
	    private String ifsc;

	    @NotBlank(message = "Address cannot be blank")
	    @Size(
	        min = 5,
	        max = 100,
	        message = "Address must be between 5 to 100 characters"
	    )
	    private String address;

	    @NotBlank(message = "Employee ID cannot be blank")
	    private String employeeId;
}

