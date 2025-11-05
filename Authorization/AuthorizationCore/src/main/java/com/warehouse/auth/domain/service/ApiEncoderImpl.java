package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.ApiKey;

public class ApiEncoderImpl implements ApiKeyEncoder {

    @Override
    public ApiKey encode(final User user) {
        return null;
    }

    @Override
    public User decode(final ApiKey apiKey) {
        return null;
    }
}
