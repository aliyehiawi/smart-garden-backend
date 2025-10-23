package org.smartgarden.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String TEST_SECRET = "test-secret-key-that-is-long-enough-for-hs256-algorithm";
    private static final int EXPIRATION_MINUTES = 120;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(TEST_SECRET, EXPIRATION_MINUTES);
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        String username = "testuser";
        Map<String, Object> claims = Map.of("role", "USER");

        String token = jwtUtil.generateToken(username, claims);

        assertNotNull(token);
        Jws<Claims> parsedToken = jwtUtil.parseToken(token);
        assertEquals(username, parsedToken.getBody().getSubject());
        assertEquals("USER", parsedToken.getBody().get("role"));
    }

    @Test
    void parseToken_withValidToken_shouldReturnClaims() {
        String username = "admin";
        Map<String, Object> claims = Map.of("role", "ADMIN", "customClaim", "value");
        String token = jwtUtil.generateToken(username, claims);

        Jws<Claims> result = jwtUtil.parseToken(token);

        assertNotNull(result);
        assertEquals(username, result.getBody().getSubject());
        assertEquals("ADMIN", result.getBody().get("role"));
        assertEquals("value", result.getBody().get("customClaim"));
        assertNotNull(result.getBody().getIssuedAt());
        assertNotNull(result.getBody().getExpiration());
    }

    @Test
    void parseToken_withExpiredToken_shouldThrowExpiredJwtException() {
        JwtUtil shortLivedJwtUtil = new JwtUtil(TEST_SECRET, 0);
        String token = shortLivedJwtUtil.generateToken("user", Map.of("role", "USER"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThrows(ExpiredJwtException.class, () -> jwtUtil.parseToken(token));
    }

    @Test
    void parseToken_withInvalidSignature_shouldThrowSignatureException() {
        String username = "testuser";
        Map<String, Object> claims = Map.of("role", "USER");
        String token = jwtUtil.generateToken(username, claims);

        JwtUtil differentSecretJwtUtil = new JwtUtil(
                "different-secret-key-that-is-also-long-enough-for-hs256",
                EXPIRATION_MINUTES
        );

        assertThrows(SignatureException.class, () -> differentSecretJwtUtil.parseToken(token));
    }

    @Test
    void parseToken_withMalformedToken_shouldThrowMalformedJwtException() {
        String malformedToken = "not.a.valid.jwt.token";

        assertThrows(MalformedJwtException.class, () -> jwtUtil.parseToken(malformedToken));
    }

    @Test
    void parseToken_withEmptyToken_shouldThrowException() {
        String emptyToken = "";

        assertThrows(Exception.class, () -> jwtUtil.parseToken(emptyToken));
    }
}

