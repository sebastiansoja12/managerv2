package com.warehouse.returntoken.domain.vo;

import java.time.Instant;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.infrastructure.adapter.secondary.entity.ReturnTokenEntity;
import com.warehouse.returntoken.infrastructure.adapter.secondary.entity.ReturnTokenId;

import lombok.Builder;

@Builder
public class ReturnToken {
    private ReturnTokenId returnTokenId;
    private final String token;
    private ShipmentId shipmentId;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant updatedAt;

    public ReturnToken(final String token) {
        this.token = token;
    }

    public ReturnToken(
            final ReturnTokenId returnTokenId,
            final String token,
            final ShipmentId shipmentId,
            final Instant expiresAt,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.returnTokenId = returnTokenId;
        this.token = token;
        this.shipmentId = shipmentId;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ReturnToken empty() {
        return new ReturnToken(StringUtils.EMPTY);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public ReturnTokenId getReturnTokenId() {
        return returnTokenId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getToken() {
        return token;
    }

    public static ReturnToken from(final ReturnTokenEntity returnTokenEntity) {
        return new ReturnToken(returnTokenEntity.getToken());
    }
}
