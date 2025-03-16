package com.test.gateway.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/fallback")
public class FallbackController {
	
	@GetMapping("/service-unavailable")
	public ResponseEntity<String> getServiceUnavailableError() {
		return new ResponseEntity<String>("<h2>Service is temporarily unavailable. Please try again later. </h2>", HttpStatus.SERVICE_UNAVAILABLE);
	}
	
}
