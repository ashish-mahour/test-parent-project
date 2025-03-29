package com.test.gateway.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	
	@Value("${security.jwt.secret-key}")
    private String secretKey;
	
	private final Logger LOG = Logger.getLogger("JWTUtil");
	
	private final SimpleDateFormat formatter = new SimpleDateFormat();
	
	public String generateToken(String email, String username) {
		Claims claims = Jwts.claims();
		claims.setSubject(email);
		claims.put("username", username);
		Date tokenCreationTime = new Date();
		LOG.log(Level.INFO, "Token Creation Date: " + formatter.format(tokenCreationTime));
		Date tokenValidity = new Date(tokenCreationTime.getTime() + TimeUnit.DAYS.toMillis(30));
		LOG.log(Level.INFO, "Token Valid till: " + formatter.format(tokenValidity));
		return Jwts.builder()
				.addClaims(claims)
				.setExpiration(tokenValidity)
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Claims validateToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
