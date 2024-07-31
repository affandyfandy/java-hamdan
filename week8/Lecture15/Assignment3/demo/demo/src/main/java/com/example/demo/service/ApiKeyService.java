package com.example.demo.service;

import com.example.demo.entity.ApiKey;
import com.example.demo.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public boolean isValidApiKey(String keyValue) {
        return apiKeyRepository.findByKeyValue(keyValue).isPresent();
    }

    public String getUsernameForKey(String keyValue) {
        return apiKeyRepository.findByKeyValue(keyValue)
                .map(ApiKey::getUsername)
                .orElse(null);
    }

    public void updateLastUsed(String keyValue) {
        Optional<ApiKey> apiKey = apiKeyRepository.findByKeyValue(keyValue);
        apiKey.ifPresent(key -> {
            key.setLastUsed(LocalDateTime.now());
            apiKeyRepository.save(key);
        });
    }
}
