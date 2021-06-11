package com.test.auth.security.jwt;


import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.test.auth.model.User;
import com.test.auth.security.service.UserDetailsImpl;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${jwtExpirationMs}")
	private int jwtExpirationMs;
	@Autowired
	RSAConfiguration load;

	public String generateJwtToken(Authentication authentication) throws Exception {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		 final String authorities = authentication.getAuthorities().stream()
	                .map(GrantedAuthority::getAuthority)
	                .collect(Collectors.joining(","));

		return Jwts.builder().setSubject(userPrincipal.getUsername()).claim(JWTConstants.AUTHORITIES_KEY, authorities).claim(JWTConstants.CLAIM_USER_ID, userPrincipal.getId()).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.RS512, load.getPrivateKey()).compact();
	}

	public UserDetails getUserFromJwtToken(String token) throws ExpiredJwtException, UnsupportedJwtException,
			MalformedJwtException, SignatureException, IllegalArgumentException, Exception {
		Claims claims = Jwts.parser().setSigningKey(load.getPublicKey()).parseClaimsJws(token).getBody();
		User user = new User();
		user.setUsername(claims.getSubject());
		return UserDetailsImpl.build(user);
	}

	public boolean validateJwtToken(String authToken) throws Exception {
		try {
			Jwts.parser().setSigningKey(load.getPublicKey()).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
	

}