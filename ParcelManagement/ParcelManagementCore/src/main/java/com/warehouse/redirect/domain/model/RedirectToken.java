package com.warehouse.redirect.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedirectToken {

    String token;

    Instant createdDate;

    Instant expiryDate;

    Long parcelId;

    String email;

    public boolean isValid() {
        return getExpiryDate().isAfter(Instant.now());
    }
}
