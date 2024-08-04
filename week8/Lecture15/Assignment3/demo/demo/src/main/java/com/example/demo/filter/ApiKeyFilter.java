package com.example.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import com.example.demo.service.ApiKeyService;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            String apiKey = request.getHeader("api-key");
            if (apiKey == null || !apiKeyService.isValidApiKey(apiKey)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"timestamp\": \"" + LocalDateTime.now() + "\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Invalid API Key\", \"path\": \"" + request.getRequestURI() + "\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
