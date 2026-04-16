package com.warehouse.auth.domain.vo;

import com.warehouse.auth.domain.model.User;

public record UsernamePasswordAuthentication(String username, String password) {
    public static UsernamePasswordAuthentication from(final User user) {
        return new UsernamePasswordAuthentication(user.getUsername(), user.getPassword());
    }
}
