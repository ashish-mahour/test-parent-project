package com.test.gateway.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.test.gateway.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	
	private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	private final Logger LOG = Logger.getLogger("JWTUtil");
	
	private final SimpleDateFormat formatter = new SimpleDateFormat();
	
	public String generateToken(User user) {
		Claims claims = Jwts.claims();
		claims.setSubject(user.getEmail());
		claims.put("name", user.getName());
		Date tokenCreationTime = new Date();
		LOG.log(Level.INFO, "Token Creation Date: " + formatter.format(tokenCreationTime));
		Date tokenValidity = new Date(tokenCreationTime.getTime() + TimeUnit.DAYS.toMillis(30));
		LOG.log(Level.INFO, "Token Valid till: " + formatter.format(tokenValidity));
		return Jwts.builder()
				.addClaims(claims)
				.setExpiration(tokenValidity)
				.signWith(SECRET_KEY)
				.compact();
	}
	
	public Claims validateToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(SECRET_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

}
