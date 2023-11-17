package com.warehouse.auth.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RefreshToken {
    private String token;
    private Instant createdDate;
    private Instant expiryDate;
    private String username;
    private boolean expired;
    private boolean revoked;

    public boolean isActual() {
        return !isExpired() && !isRevoked();
    }
}
