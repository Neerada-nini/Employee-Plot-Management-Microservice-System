package com.microservices.test.externalClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

	
	    private String id;


	    private String name;

	    private String address;

	    private double salary;

	    private String dateTime;
	}
