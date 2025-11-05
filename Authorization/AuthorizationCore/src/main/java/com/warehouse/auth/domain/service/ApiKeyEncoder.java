package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.ApiKey;

public interface ApiKeyEncoder {
    ApiKey encode(final User user);
    User decode(final ApiKey apiKey);
}
