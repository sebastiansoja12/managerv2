package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.User;

public interface RefreshTokenGenerator {
    String generateToken(User user);
}
