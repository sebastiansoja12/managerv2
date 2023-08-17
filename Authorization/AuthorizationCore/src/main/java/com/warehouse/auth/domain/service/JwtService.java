package com.warehouse.auth.domain.service;

import java.util.Map;

import com.warehouse.auth.domain.model.User;

public interface JwtService {
    String extractUsername(String jwt);

    String generateToken(Map<String, Object> extraClaims, User user, Long expiration);

    String generateToken(User user);

    boolean isTokenValid(String token, User user);
}
