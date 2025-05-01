package com.test.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.tracing.zipkin.ZipkinAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import reactor.core.publisher.Hooks;

@SpringBootApplication
@EnableEurekaServer
@EnableAutoConfiguration(exclude = ZipkinAutoConfiguration.class)
public class TestProjectServer {

	public static void main(String[] args) {
		SpringApplication.run(TestProjectServer.class, args);
		Hooks.enableAutomaticContextPropagation();
	}
}
