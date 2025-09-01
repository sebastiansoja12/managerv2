package com.warehouse.auth.domain.service;

import org.springframework.stereotype.Service;

import com.warehouse.auth.domain.port.secondary.UserRepository;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    private final UserRepository userRepository;

    public ApiKeyServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validateApiKey(final String key) {
        if (userRepository.findByApiKey(key) == null) {
            throw new IllegalArgumentException("Invalid API key");
        }
    }
}
