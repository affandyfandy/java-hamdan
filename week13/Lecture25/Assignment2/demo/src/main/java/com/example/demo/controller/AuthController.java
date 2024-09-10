package com.example.demo.controller;

import com.example.demo.entity.model.User;
import com.example.demo.service.AuthService;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtEncoder jwtEncoder;

    public AuthController(AuthService authService, JwtEncoder jwtEncoder) {
        this.authService = authService;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return authService.registerUser(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        Optional<User> validatedUser = authService.validateUser(user.getUsername(), user.getPassword());

        if (validatedUser.isPresent()) {
            Instant now = Instant.now();
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(3600))
                    .subject(validatedUser.get().getUsername())
                    .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", token);
            return response;
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }
}