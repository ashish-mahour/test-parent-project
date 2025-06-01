package com.test.gateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.test.gateway.entities.User;
import com.test.gateway.models.ErrorResponse;
import com.test.gateway.models.UserRequest;
import com.test.gateway.models.UserResponse;
import com.test.gateway.services.AuthService;
import com.test.gateway.services.JWTUtil;

import io.micrometer.tracing.Tracer;
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
	
	@Autowired
	private Tracer tracer;
	
	@GetMapping("/csrf")
	public Mono<ResponseEntity<CsrfToken>> getCSRFToken(ServerWebExchange exchange) {
		return Mono.just(ResponseEntity.ok(exchange.getAttribute("csrf_token")));
	}
	
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<? extends Object>> login(@RequestBody UserRequest user) {
		try {
			Mono<Authentication> authMono = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			return authMono.flatMap(auth -> {
				if (auth == null || auth.isAuthenticated()) return Mono.just(ResponseEntity.internalServerError().body(new ErrorResponse("User not found.")));
				return Mono.just(ResponseEntity.ok().body(new UserResponse(jwtUtil.generateToken(auth.getPrincipal().toString(), user.getUsername()))));
			}).onErrorResume(e -> {
				return Mono.just(ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(), tracer.currentSpan().context().traceId(), tracer.currentSpan().context().spanId())));
			});
		} catch (Exception e) {
			return Mono.just(ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(), tracer.currentSpan().context().traceId(), tracer.currentSpan().context().spanId())));
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
