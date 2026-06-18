package com.warehouse.auth.domain.vo;

import com.warehouse.auth.domain.model.User;

public record CurrentUserAuthentication(String jwtToken, User user) {
}
