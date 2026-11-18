package com.warehouse.commonassets.security;


public interface AttemptLimiter {

    boolean isBlocked(final String key);

    void recordAttempt(final String key);

    void reset(final String key);
}
