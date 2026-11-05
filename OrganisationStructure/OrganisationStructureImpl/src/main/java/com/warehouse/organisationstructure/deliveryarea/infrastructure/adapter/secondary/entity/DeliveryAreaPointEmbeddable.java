package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class DeliveryAreaPointEmbeddable {

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    protected DeliveryAreaPointEmbeddable() {
    }

    public DeliveryAreaPointEmbeddable(final Double latitude, final Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
