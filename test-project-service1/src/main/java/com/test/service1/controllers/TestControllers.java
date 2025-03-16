package com.test.service1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service1")
public class TestControllers {
	
	@GetMapping("/print")
	public String printMessage(@RequestParam("name") String name) {
		return "Hello " + name;
	}
}
