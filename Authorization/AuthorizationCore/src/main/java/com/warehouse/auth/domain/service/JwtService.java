package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractUsername(String authHeader);

    String generateToken(Map<String, Object> extraClaims, User user, Long expiration);

    String generateToken(User user);

    boolean isTokenValid(String token, User user);
}
