package com.test.service2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TestProjectService2 {

	public static void main(String[] args) {
		setRandomPort(10000, 20000);
		SpringApplication.run(TestProjectService2.class, args);
	}

	private static void setRandomPort(int i, int j) {
	}

}
