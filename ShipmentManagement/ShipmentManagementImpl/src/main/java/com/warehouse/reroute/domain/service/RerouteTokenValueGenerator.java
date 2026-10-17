package com.warehouse.reroute.domain.service;

import java.security.SecureRandom;

import com.warehouse.reroute.domain.vo.RerouteTokenValue;

public class RerouteTokenValueGenerator {

    private static final int TOKEN_BOUND = 1_000_000;

    private final SecureRandom secureRandom = new SecureRandom();

    public RerouteTokenValue generate() {
        return new RerouteTokenValue(secureRandom.nextInt(TOKEN_BOUND));
    }
}
