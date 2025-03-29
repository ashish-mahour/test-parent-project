package com.test.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.LogoutSpec;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig {
	
	@Autowired
	private JWTAuthExceptionHandlerEntryPoint entrypoint;

	@Autowired
	private JWTAutoFilter jwtAutoFilter;
	
	@Bean
	public SecurityWebFilterChain getSecurityFilterChain(ServerHttpSecurity http) {
		return http.csrf(specs -> specs.disable())
				.cors(config -> config.disable())
				.authorizeExchange(exchange -> exchange
						.pathMatchers(HttpMethod.POST, "/api/auth/*").permitAll()
						.anyExchange().authenticated())
				.httpBasic(HttpBasicSpec::disable)
				.formLogin(FormLoginSpec::disable)
				.logout(LogoutSpec::disable)
				.exceptionHandling(expection -> expection.authenticationEntryPoint(entrypoint))
				.addFilterBefore(jwtAutoFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.authenticationManager(getReactiveAuthenticationManager())
				.build();
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ReactiveAuthenticationManager getReactiveAuthenticationManager() {
		return new CustomAuthenticationProvider();
	}
}
