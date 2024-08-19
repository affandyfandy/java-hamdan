package com.example.auth.service;

import com.example.auth.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public boolean verifyApiKey(String apiKeyValue) {
        return apiKeyRepository.findByApiKeyValue(apiKeyValue).isPresent();
    }
}
