package com.test.gateway.config;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JWTAutoFilter implements WebFilter {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	private final Logger LOG = Logger.getLogger(JWTAutoFilter.class.getName());
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();
		final String requestToken = request.getHeaders().getFirst("Authorization");
		SecurityContext context = SecurityContextHolder.getContext();
		try {
			if (requestToken != null && requestToken.startsWith("Bearer") && context.getAuthentication() == null) {
				final String token = requestToken.substring(7);
				Claims claims = jwtUtil.validateToken(token);
				LOG.log(Level.INFO, claims.toString());
				Authentication auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), claims.get("password"), List.of());
				context.setAuthentication(auth);

				return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
			} else {
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
		}
		return chain.filter(exchange);
	}

}
