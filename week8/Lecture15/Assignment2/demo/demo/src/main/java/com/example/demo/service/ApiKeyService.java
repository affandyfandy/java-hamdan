package com.example.demo.service;

import com.example.demo.model.ApiKey;
import com.example.demo.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyService {
    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public boolean isValidApiKey(String key){
        return apiKeyRepository.findByKeyValue(key).isPresent();
    }
}
