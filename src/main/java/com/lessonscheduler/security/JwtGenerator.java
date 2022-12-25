package com.lessonscheduler.security;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.lessonscheduler.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtGenerator {

    public String generateJWT(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + Constants.JWT_EXPIRATION);

        String token = Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(expireDate)
        .signWith(Constants.key)
        .compact();

        return token;
    }

    public String getUserNameFromJwt(String token) {
        Jws<Claims> jws = Jwts.parserBuilder()  // (1)
        .setSigningKey(Constants.key)         // (2)
        .build()                    // (3)
        .parseClaimsJws(token);

        return jws.getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(Constants.key).build().parseClaimsJws(token);
            // If it got till return it means that we can trust the token.
            return true;
        } catch (JwtException e) {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
        }
    }
    
}
