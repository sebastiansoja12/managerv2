package com.warehouse.returntoken.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ShipmentId;

public class ReturnTokenEntity {
    private ReturnTokenId returnTokenId;
    private String token;
    private ShipmentId shipmentId;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant updatedAt;

    public String getToken() {
        return token;
    }
}
