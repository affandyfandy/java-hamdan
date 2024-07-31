package com.example.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String username = request.getHeader("username");
            if (username == null || username.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"timestamp\": \"" + LocalDateTime.now() + "\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Username is required\", \"path\": \"" + request.getRequestURI() + "\"}");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.addHeader("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
