package com.warehouse.auth.domain.service;

public interface ApiKeyService {
    void validateApiKey(final String key);
}
