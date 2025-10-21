package org.smartgarden.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.smartgarden.backend.entity.Device;
import org.smartgarden.backend.repository.DeviceRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class DeviceApiKeyFilter extends OncePerRequestFilter {

    private final DeviceRepository deviceRepository;

    public DeviceApiKeyFilter(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return !path.matches("/api/v1/devices/[^/]+/(data|commands(/.*)?)");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String deviceId = request.getRequestURI().split("/api/v1/devices/")[1].split("/")[0];
        String apiKeyHeader = request.getHeader("X-DEVICE-KEY");
        if (apiKeyHeader == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Device device = deviceRepository.findByDeviceId(deviceId).orElse(null);
        if (device == null || !device.isEnabled() || !apiKeyHeader.equals(device.getApiKey())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("device:" + deviceId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}


