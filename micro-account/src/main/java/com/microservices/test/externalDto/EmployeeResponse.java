package com.microservices.test.externalDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeResponse{

   
    private String id;


    private String name;


    private String address;


    private double salary;

    private String dateTime;
}