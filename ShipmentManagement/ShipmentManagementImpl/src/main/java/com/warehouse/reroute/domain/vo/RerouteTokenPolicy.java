package com.warehouse.reroute.domain.vo;

import java.time.Duration;

public record RerouteTokenPolicy(Duration validity, int maxFailedAttempts) {
}
