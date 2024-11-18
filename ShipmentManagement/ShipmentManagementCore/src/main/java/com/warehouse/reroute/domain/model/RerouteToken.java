package com.warehouse.reroute.domain.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RerouteToken {

    private Long id;

    private Integer token;

    private Instant createdDate;

    private Instant expiryDate;

    private Long parcelId;

    private String email;

    public boolean isValid() {
        return getExpiryDate().isAfter(Instant.now());
    }

    public void invalidate() {
        this.expiryDate = Instant.now();
    }
}
