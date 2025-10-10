package com.warehouse.returning.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ApiKeyService {

    public String extractTenantFromApiKey(final String apiKey) {
        if (StringUtils.isEmpty(apiKey)) {
            throw new IllegalArgumentException("API key is missing");
        }

        final String[] parts = apiKey.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid API key format");
        }

        final String tenantCode = parts[0].trim();
        if (tenantCode.isEmpty()) {
            throw new IllegalArgumentException("Tenant code missing in API key");
        }

        return tenantCode;
    }
}

