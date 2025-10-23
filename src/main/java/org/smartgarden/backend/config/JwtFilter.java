package org.smartgarden.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.smartgarden.backend.repository.UserRepository;
import org.smartgarden.backend.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT authentication filter for Spring Security.
 * 
 * <p>This filter intercepts HTTP requests and validates JWT tokens
 * from the Authorization header. If a valid token is found, the user
 * is authenticated in the Spring Security context.
 * 
 * <p>The filter excludes certain paths that don't require authentication:
 * <ul>
 *   <li>/api/v1/auth/** - Authentication endpoints</li>
 *   <li>/api/v1/devices/** - Device API key authentication</li>
 *   <li>/h2-console - H2 database console</li>
 *   <li>/swagger-ui/** - Swagger UI</li>
 *   <li>/v3/api-docs/** - OpenAPI documentation</li>
 * </ul>
 * 
 * @author Smart Garden Team
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * Constructs a new JwtFilter with required dependencies.
     * 
     * @param jwtUtil utility for JWT operations
     * @param userRepository repository for user validation
     */
    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * Processes each HTTP request to validate JWT tokens.
     * 
     * <p>Extracts the JWT token from the Authorization header,
     * validates it, and sets the authentication in the security context
     * if valid. Excluded paths are allowed through without authentication.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/api/v1/auth/")
                || path.startsWith("/api/v1/devices/")
                || path.startsWith("/h2-console")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            processJwtToken(header);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts and validates JWT token, setting authentication context if valid.
     * 
     * @param header the Authorization header containing the Bearer token
     */
    private void processJwtToken(String header) {
        String token = header.substring(7);
        try {
            Jws<Claims> jws = jwtUtil.parseToken(token);
            String username = jws.getBody().getSubject();
            String role = (String) jws.getBody().get("role");
            
            if (username != null && role != null 
                    && userRepository.findByUsername(username).isPresent()) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.warn("JWT token validation failed - user not found or invalid claims: {}", 
                        username);
            }
        } catch (Exception e) {
            log.warn("Failed to authenticate request - invalid or expired token");
        }
    }
}


