package com.warehouse.auth.domain.service;

import java.util.Map;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.adapter.secondary.enumeration.Role;

public interface JwtService {
    String extractUsername(String jwt);

    String generateToken(Map<String, Object> extraClaims, User user, Long expiration);

    String generateToken(User user);

    String generateToken(final String firstName, final String username, final Role role);

    boolean isTokenValid(String token, User user);
}
