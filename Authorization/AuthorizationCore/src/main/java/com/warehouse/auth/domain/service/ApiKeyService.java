package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.vo.DecodedApiTenant;

public interface ApiKeyService {
    void validateApiKey(final String key);
    DecodedApiTenant decodeJwt(final String token);
    DecodedApiTenant decodeApiKey(final String apiKey);
}
