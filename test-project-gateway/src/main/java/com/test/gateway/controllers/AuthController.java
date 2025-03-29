package com.test.gateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.gateway.config.JWTUtil;
import com.test.gateway.entities.User;
import com.test.gateway.models.ErrorResponse;
import com.test.gateway.models.UserRequest;
import com.test.gateway.models.UserResponse;
import com.test.gateway.services.AuthService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private ReactiveAuthenticationManager authenticationManager;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<? extends Object>> login(@RequestBody UserRequest user) {
		try {
			Mono<Authentication> authMono = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			return authMono.flatMap(auth -> {
				if (auth == null || auth.isAuthenticated()) return Mono.just(ResponseEntity.internalServerError().body(new ErrorResponse("User not found.")));
				return Mono.just(ResponseEntity.ok().body(new UserResponse(jwtUtil.generateToken(auth.getPrincipal().toString(), user.getUsername()))));
			}).onErrorResume(e -> {
				return Mono.just(ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage())));
			});
		} catch (Exception e) {
			return Mono.just(ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage())));
		}
		
	}
	
	@PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Mono<User>> signUp(@RequestBody User user) {
		try {
			return ResponseEntity.ok().body(authService.signupUser(user));	
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
}
