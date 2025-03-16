package com.test.service1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TestService1 {

	public static void main(String[] args) {
		SpringApplication.run(TestService1.class, args);
	}
}
