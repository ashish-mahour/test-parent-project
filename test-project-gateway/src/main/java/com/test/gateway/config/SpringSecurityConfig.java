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
import org.springframework.security.web.server.csrf.ServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.WebSessionServerCsrfTokenRepository;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfig {
	
	@Autowired
	private JWTAuthExceptionHandlerEntryPoint entrypoint;

	@Autowired
	private JWTAutoFilter jwtAutoFilter;
	
//	@Autowired
//	private ServerCsrfTokenRepository serverCsrfTokenRepository;
	
	@Bean
	public SecurityWebFilterChain getSecurityFilterChain(ServerHttpSecurity http) {
		return http
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.cors(ServerHttpSecurity.CorsSpec::disable)
				.authorizeExchange(exchange -> exchange
						.pathMatchers(HttpMethod.GET, "/api/auth/csrf").permitAll()
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
	
//	@Bean
//	public ServerCsrfTokenRepository getCsrfTokenRepository(ServerWebExchange exchange) {
//		WebSessionServerCsrfTokenRepository csrfTokenRepo = new WebSessionServerCsrfTokenRepository();
//		csrfTokenRepo.setHeaderName("csrf_token");
//		csrfTokenRepo.generateToken(exchange);
//		return csrfTokenRepo;
//	}
	
	public NegatedServerWebExchangeMatcher getURLsForDisabledCSRF() {
        return new NegatedServerWebExchangeMatcher(exchange -> ServerWebExchangeMatchers.pathMatchers("/api/auth/csrf").matches(exchange));
    }
}
