package com.microservices.test;

import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
@EnableDiscoveryClient
public class PlotGeneralConfiguration {

		@Bean
		public ModelMapper modelMapper() {
			
	return new ModelMapper();
	}
}
