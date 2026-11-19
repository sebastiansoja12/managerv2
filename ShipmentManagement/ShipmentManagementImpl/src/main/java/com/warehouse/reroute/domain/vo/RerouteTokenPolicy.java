package com.warehouse.reroute.domain.vo;

import java.time.Duration;

public record RerouteTokenPolicy(Duration validity, int maxFailedAttempts, int maxIssuedTokens, Duration issueWindow) {

    public boolean allowsIssuing(final long issuedInWindow) {
        return issuedInWindow < maxIssuedTokens;
    }
}
