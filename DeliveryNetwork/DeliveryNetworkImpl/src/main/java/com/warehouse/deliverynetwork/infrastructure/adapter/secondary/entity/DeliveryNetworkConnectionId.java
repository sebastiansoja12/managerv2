package com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record DeliveryNetworkConnectionId(Long value) implements Serializable {

    public static DeliveryNetworkConnectionId generate() {
        return new DeliveryNetworkConnectionId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
