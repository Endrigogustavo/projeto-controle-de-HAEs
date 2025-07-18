package br.com.fateczl.apihae.useCase.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.fateczl.apihae.domain.entity.Employee;


@Component
public class JWTUtils {

    private final String jwtSecret;
    private final Algorithm algorithm;

    public JWTUtils(@Value("${app.jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
        this.algorithm = Algorithm.HMAC256(jwtSecret); 
    }

    public String generateToken(Employee employee) {
        try {
            return JWT.create()
                .withIssuer("API HAE")
                .withSubject(employee.getId().toString())
                .withExpiresAt(generateExpirationDate())
                .sign(algorithm);
        } catch (JWTCreationException exc) {
            throw new RuntimeException("Error while authenticating: " + exc.getMessage(), exc);
        }
    }

    public String validateToken(String token) {
        try {
            return JWT.require(algorithm)
                .withIssuer("API HAE")
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException exc) {
            return null;
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now()
            .plusHours(6)
            .toInstant(ZoneOffset.of("-03:00"));
    }

    public String decodeToken(String token) {
        try {
            return JWT.decode(token).getSubject();
        } catch (JWTVerificationException exc) {
            return null;
        }
    }

}
