
package com.careerforge.backend;

import com.careerforge.backend.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTests {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void shouldGenerateToken() {
        String token = jwtService.generateToken("test@example.com");

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals("test@example.com", jwtService.extractEmail(token));
    }

    @Test
    void shouldExtractEmailFromToken() {
        String token = jwtService.generateToken("vaishnavi@example.com");

        assertEquals(
                "vaishnavi@example.com",
                jwtService.extractEmail(token)
        );
    }

    @Test
    void shouldRejectMalformedToken() {
        assertThrows(
                Exception.class,
                () -> jwtService.extractEmail("not-a-valid-jwt")
        );
    }

    @Test
    void shouldRejectTokenSignedWithDifferentKey() {
        SecretKey differentKey = Keys.hmacShaKeyFor(
                "AnotherSecretKeyForJWTAuthentication2026Secure"
                        .getBytes(StandardCharsets.UTF_8)
        );

        String token = Jwts.builder()
                .subject("test@example.com")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(differentKey)
                .compact();

        assertThrows(
                Exception.class,
                () -> jwtService.extractEmail(token)
        );
    }
}