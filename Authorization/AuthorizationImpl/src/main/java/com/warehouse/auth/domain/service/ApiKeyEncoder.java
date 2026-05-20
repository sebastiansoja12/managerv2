package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.ApiKey;
import com.warehouse.commonassets.identificator.UserId;

public interface ApiKeyEncoder {
    ApiKey encode(final User user);
    ApiKey encode(final UserId userId, final String apiKey);
    ApiKey decode(final ApiKey apiKey);
}
