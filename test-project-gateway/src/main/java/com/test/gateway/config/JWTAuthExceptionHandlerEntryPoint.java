package com.test.gateway.config;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JWTAuthExceptionHandlerEntryPoint implements ServerAuthenticationEntryPoint {
	
	private Logger LOG = LoggerFactory.getLogger(getClass());

	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
	 	ServerHttpResponse response = exchange.getResponse();
	 	LOG.error(ex.getMessage());
	 	response.writeWith(Mono.just(response.bufferFactory().wrap(ex.getMessage().getBytes(StandardCharsets.UTF_8))));
	 	response.setStatusCode(HttpStatus.UNAUTHORIZED);
		return response.setComplete();
	}

}
