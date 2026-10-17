package com.warehouse.reroute.domain.vo;

import com.warehouse.reroute.domain.exception.InvalidRerouteTokenException;

public record RerouteTokenValue(int value) {

    private static final int MAX_VALUE = 999_999;

    public RerouteTokenValue {
        if (value < 0 || value > MAX_VALUE) {
            throw new InvalidRerouteTokenException();
        }
    }

    public static RerouteTokenValue parse(final String token) {
        if (token == null || !token.trim().matches("\\d{6}")) {
            throw new InvalidRerouteTokenException();
        }
        return new RerouteTokenValue(Integer.parseInt(token.trim()));
    }

    public String formatted() {
        return "%06d".formatted(value);
    }
}
