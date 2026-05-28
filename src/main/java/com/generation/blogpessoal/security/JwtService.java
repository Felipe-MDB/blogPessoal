package com.generation.blogpessoal.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	
	private String secret = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	private Duration expiration = Duration.ofMinutes(60);
	
	private SecretKey signinKey;

	private SecretKey getSigninKey() {
		if (signinKey == null) {
			byte[] KeyBytes = Decoders.BASE64.decode(secret);
			signinKey = Keys.hmacShaKeyFor(KeyBytes);
		}
		return signinKey;
	}
	
	  private Claims extractAllClaims(String token) {
	        return Jwts.parser()
	            .verifyWith(getSigninKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload();
	    }

	    public String extractUsername(String token) {
	        return extractAllClaims(token).getSubject();
	    }

	    public Date extractExpiration(String token) {
	        return extractAllClaims(token).getExpiration();
	    }

	    public boolean validateToken(String token, UserDetails userDetails) {
	        Claims claims = extractAllClaims(token);
	        return claims.getSubject().equals(userDetails.getUsername()) && 
	               claims.getExpiration().after(new Date());
	    }

	    public String generateToken(String username) {
	        Instant now = Instant.now();
	        return Jwts.builder()
	            .subject(username)
	            .issuedAt(Date.from(now))
	            .expiration(Date.from(now.plus(expiration)))
	            .signWith(getSigninKey())
	            .compact();
	    }
}
