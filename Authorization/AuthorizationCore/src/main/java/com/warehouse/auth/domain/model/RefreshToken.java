package com.warehouse.auth.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshToken {
    private String token;
    private LocalDateTime createdDate;
    private LocalDateTime expiryDate;
    private String username;
    private boolean expired;
    private boolean revoked;

    public boolean isActual() {
        return !isExpired() && !isRevoked();
    }
}
