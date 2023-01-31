package com.warehouse.auth.domain.service;

import org.springframework.security.core.Authentication;

public interface JwtProvider {

    String generateToken(Authentication authentication);

    boolean validateToken(String token);

    String getUsernameFromJwt(String jwt);

    String generateTokenWithUsername(String username);
}
