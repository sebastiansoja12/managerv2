package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.infrastructure.adapter.primary.dto.UsernameDto;

public record Username(String value) {
    public static Username from(final UsernameDto username) {
        return new Username(username.value());
    }
}
