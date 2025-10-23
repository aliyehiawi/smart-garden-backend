package org.smartgarden.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * Utility class for JWT (JSON Web Token) operations.
 * 
 * <p>This class provides functionality to generate and parse JWT tokens
 * using the HS256 signing algorithm. Tokens are used for user authentication
 * and contain user identity and role information.
 * 
 * <p>This class is marked as {@code final} to prevent finalizer attacks
 * on the security-sensitive cryptographic key material.
 * 
 * @author Smart Garden Team
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public final class JwtUtil {

    private final Key key;
    private final int expirationMinutes;

    /**
     * Constructs a new JwtUtil with the specified secret and expiration time.
     * 
     * @param secret the secret key used for signing tokens (minimum 256 bits)
     * @param expirationMinutes the token validity duration in minutes (default: 120)
     */
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration.minutes:120}") int expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    /**
     * Generates a JWT token with the specified subject and claims.
     * 
     * <p>The generated token includes:
     * <ul>
     *   <li>Subject: typically the username</li>
     *   <li>Custom claims: additional data like user role</li>
     *   <li>Issued at: current timestamp</li>
     *   <li>Expiration: current time plus expiration minutes</li>
     * </ul>
     * 
     * @param subject the token subject (typically username)
     * @param claims additional custom claims to include in the token
     * @return the generated JWT token as a compact string
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        Instant expiration = now.plus(expirationMinutes, ChronoUnit.MINUTES);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Parses and validates a JWT token.
     * 
     * <p>This method verifies the token's signature and expiration,
     * then extracts and returns the claims. If validation fails,
     * specific exceptions are thrown.
     * 
     * @param token the JWT token string to parse
     * @return the parsed JWT with claims
     * @throws ExpiredJwtException if the token has expired
     * @throws SignatureException if the token signature is invalid
     * @throws MalformedJwtException if the token is malformed
     * @throws JwtException if any other JWT-related error occurs
     */
    public Jws<Claims> parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            
        } catch (ExpiredJwtException e) {
            log.warn("JWT token has expired - subject: {}", e.getClaims().getSubject());
            throw e;
        } catch (SignatureException e) {
            log.error("JWT token signature validation failed - invalid signature");
            throw e;
        } catch (MalformedJwtException e) {
            log.error("JWT token is malformed - {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            log.error("JWT token validation failed - {}", e.getMessage());
            throw e;
        }
    }
}


