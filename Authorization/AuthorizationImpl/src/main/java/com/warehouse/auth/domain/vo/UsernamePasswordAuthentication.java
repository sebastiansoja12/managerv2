package com.warehouse.auth.domain.vo;

import com.warehouse.auth.domain.model.OrganizationUser;

public record UsernamePasswordAuthentication(String username, String password) {
    public static UsernamePasswordAuthentication from(final OrganizationUser user) {
        return new UsernamePasswordAuthentication(user.getUsername(), user.getPassword());
    }
}
