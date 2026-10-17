package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.OrganizationUser;

public interface RefreshTokenGenerator {
    String generateToken(OrganizationUser user);
}
