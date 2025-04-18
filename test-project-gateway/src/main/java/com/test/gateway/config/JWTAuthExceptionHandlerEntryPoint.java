package com.test.gateway.config;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JWTAuthExceptionHandlerEntryPoint implements ServerAuthenticationEntryPoint {

	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
	 	ServerHttpResponse response = exchange.getResponse();
	 	response.writeWith(Mono.just(response.bufferFactory().wrap(ex.getMessage().getBytes(StandardCharsets.UTF_8))));
	 	response.setStatusCode(HttpStatus.UNAUTHORIZED);
		return response.setComplete();
	}

}
