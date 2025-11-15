package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.User;
import com.warehouse.commonassets.identificator.DepartmentCode;

import java.util.Map;

public interface JwtService {
    String extractUsername(String jwt);

    String generateToken(Map<String, Object> extraClaims, User user, Long expiration);

    String generateToken(final User user);

    String generateToken(final String firstName, final String username, final User.Role role, final DepartmentCode departmentCode);

    boolean isTokenValid(String token, User user);
}
