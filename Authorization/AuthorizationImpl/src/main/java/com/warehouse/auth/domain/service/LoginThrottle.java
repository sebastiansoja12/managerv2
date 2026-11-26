package com.warehouse.auth.domain.service;

import java.util.Locale;

import com.warehouse.commonassets.security.AttemptLimiter;

/**
 * Slows down password guessing: failed logins are counted per account and per client address.
 */
public class LoginThrottle {

    private final AttemptLimiter accountLimiter;

    private final AttemptLimiter clientLimiter;

    public LoginThrottle(final AttemptLimiter accountLimiter, final AttemptLimiter clientLimiter) {
        this.accountLimiter = accountLimiter;
        this.clientLimiter = clientLimiter;
    }

    public boolean isBlocked(final String scope, final String username, final String clientAddress) {
        return this.accountLimiter.isBlocked(accountKey(scope, username))
                || this.clientLimiter.isBlocked(clientKey(clientAddress));
    }

    public void recordFailure(final String scope, final String username, final String clientAddress) {
        this.accountLimiter.recordAttempt(accountKey(scope, username));
        this.clientLimiter.recordAttempt(clientKey(clientAddress));
    }

    public void recordSuccess(final String scope, final String username) {
        this.accountLimiter.reset(accountKey(scope, username));
    }

    private String accountKey(final String scope, final String username) {
        return scope + ":" + (username == null ? "" : username.trim().toLowerCase(Locale.ROOT));
    }

    private String clientKey(final String clientAddress) {
        return clientAddress == null ? "unknown" : clientAddress;
    }
}
