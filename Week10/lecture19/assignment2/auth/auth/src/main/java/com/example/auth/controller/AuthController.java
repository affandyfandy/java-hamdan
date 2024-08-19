package com.example.auth.controller;

import com.example.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/auth/verify")
    public boolean verifyApiKey(@RequestParam("api-key") String apiKey) {
        return authService.verifyApiKey(apiKey);
    }
}
