
# Assignment1:  OncePerRequestFilter in Spring

## Overview

The `OncePerRequestFilter` is a class in the Spring Framework that ensures a particular action is executed only once per request. It is particularly useful for tasks like logging, security checks, or other repetitive operations that should not occur multiple times within the same request lifecycle.

## Key Features

- **Single Execution Per Request:** Ensures the filter is executed only once per request, even if the request is forwarded multiple times.
- **Integration with Spring Security:** Often used in security scenarios to ensure security filters or logging operations are applied only once.
- **Customizable Behavior:** Developers can define custom behavior by overriding the `doFilterInternal` method.

## When to Use `OncePerRequestFilter`

`OncePerRequestFilter` is useful in scenarios where you need to ensure that a specific piece of logic is executed only once per HTTP request. Common use cases include:

1. **Logging:** Log request details like headers, parameters, and body content only once per request to avoid redundant log entries.
2. **Security Checks:** Perform authentication and authorization checks that should only be done once per request.
3. **Request Wrapping:** Wrap the `HttpServletRequest` and `HttpServletResponse` for purposes such as caching request content or modifying the response.
4. **Performance Metrics:** Collect performance metrics such as the time taken to process a request.
5. **Custom Pre-processing and Post-processing:** Perform custom logic before and after the main request processing.

## Implementation Steps

### Step 1: Maven Dependency

Ensure you have the necessary Spring dependencies in your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Step 2: Custom Filter Class

Create a class that extends `OncePerRequestFilter` and overrides the `doFilterInternal` method:

```java
package com.example.demo.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class CustomLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(CustomLoggingFilter.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        logger.info("Request URI: " + request.getRequestURI());
        
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
```

### Step 3: Configuration Class

Register the filter in your Spring configuration class:

```java
package com.example.demo.config;

import com.example.demo.filter.CustomLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class FilterConfig {

    @Bean
    public OncePerRequestFilter customLoggingFilter() {
        return new CustomLoggingFilter();
    }
}
```

### Step 4: Main Application Class

Ensure your Spring Boot application is set up to scan for configurations:

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

## Testing the Filter

Run your Spring Boot application and make a request to any endpoint. You should see log entries for each request in the console, indicating that the `CustomLoggingFilter` is executed once per request.

## When to Use `OncePerRequestFilter`
`OncePerRequestFilter` is particularly useful in scenarios where you need to ensure that a specific piece of logic is executed only once per HTTP request. This is crucial for tasks that should not be repeated within the same request lifecycle, such as logging, authentication, authorization, and other cross-cutting concerns. Here are some common use cases:

- **Logging**: To log request details like headers, parameters, and body content only once per request to avoid redundant log entries.
- **Security Checks**: To perform authentication and authorization checks that should only be done once per request.
- **Request Wrapping**: To wrap the `HttpServletRequest` and `HttpServletResponse` for purposes such as caching request content or modifying the response.
- **Performance Metrics**: To collect performance metrics such as the time taken to process a request.
- **Custom Pre-processing and Post-processing**: To perform custom logic before and after the main request processing.