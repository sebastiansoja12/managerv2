package com.warehouse.reroute.domain.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RerouteToken {

    Long id;

    Integer token;

    Instant createdDate;

    Instant expiryDate;

    Long parcelId;

    String email;

    public boolean isValid() {
        return getExpiryDate().isAfter(Instant.now());
    }
}
