package com.warehouse.returntoken.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ShipmentId;

import jakarta.persistence.*;

@Entity
@Table(name = "return_tokens")
public class ReturnTokenEntity {
    
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "return_token_id"))
    private ReturnTokenId returnTokenId;
    
    @Column(name = "token", nullable = false)
    private String token;

    @AttributeOverride(name = "value", column = @Column(name = "shipment_id"))
    private ShipmentId shipmentId;
    
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ReturnTokenEntity() {
    }

	public ReturnTokenEntity(final Instant createdAt, final Instant expiresAt, final ReturnTokenId returnTokenId,
			final ShipmentId shipmentId, final String token, final Instant updatedAt) {
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.returnTokenId = returnTokenId;
        this.shipmentId = shipmentId;
        this.token = token;
        this.updatedAt = updatedAt;
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
}
