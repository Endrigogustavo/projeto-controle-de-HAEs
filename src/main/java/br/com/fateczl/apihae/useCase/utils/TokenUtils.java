package br.com.fateczl.apihae.useCase.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.fateczl.apihae.domain.entity.Employee;

@Service
public class TokenUtils {
    
    @Value("${app.jwt.secret}")
    private String secret;

    public String generateToken(Employee user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error while authenticating");
        }
    }

    public String generateTokenId(String id){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(id)
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error while authenticating");
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            Date expirationDate = JWT.decode(token).getExpiresAt();
            return expirationDate != null && expirationDate.after(new Date());
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    public String extractUserId(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                      .withIssuer("login-auth-api")
                      .build()
                      .verify(token)
                      .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(10).toInstant(ZoneOffset.of("-03:00"));
    }

    public String decodeToken(String token) {
        try {
            return JWT.decode(token).getSubject();
        } catch (JWTVerificationException exc) {
            return null;
        }
    }
}