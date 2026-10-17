package com.warehouse.auth.domain.vo;

import com.warehouse.auth.domain.model.OrganizationUser;

public record CurrentUserAuthentication(String jwtToken, OrganizationUser user) {
}
