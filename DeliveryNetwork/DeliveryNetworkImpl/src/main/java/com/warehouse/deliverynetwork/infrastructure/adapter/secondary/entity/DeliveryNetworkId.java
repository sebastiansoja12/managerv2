package com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record DeliveryNetworkId(Long value) implements Serializable {

    public static DeliveryNetworkId generate() {
        return new DeliveryNetworkId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
