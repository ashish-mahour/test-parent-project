package com.test.service2.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service2")
public class TestController {
		
	@GetMapping("/print")
	public String printMessage(@RequestParam("name") String name) {
		return "Namaste " + name;
	}
}
