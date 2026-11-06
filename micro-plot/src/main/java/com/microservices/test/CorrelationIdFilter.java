package com.microservices.test;


import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter{

	public static final String CORRELATION_ID_HEADER="X-Correlation-ID";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String correlationId=request.getHeader(CORRELATION_ID_HEADER);
		
		if (correlationId ==null || !correlationId.matches("[a-zA-Z0-9._-]{1,100}")) {
			correlationId = UUID.randomUUID().toString();
		}
		
		request.setAttribute(CORRELATION_ID_HEADER,  correlationId);
		response.setHeader(CORRELATION_ID_HEADER, correlationId);
		filterChain.doFilter(request, response);
		
		
		
	}
}
