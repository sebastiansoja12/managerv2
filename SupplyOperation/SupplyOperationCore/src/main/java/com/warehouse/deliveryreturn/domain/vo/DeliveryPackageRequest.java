package com.warehouse.deliveryreturn.domain.vo;

import lombok.Builder;

@Builder
public class DeliveryPackageRequest {
    private final DeliveryReturnInformation delivery;

    public DeliveryPackageRequest(final DeliveryReturnInformation delivery) {
        this.delivery = delivery;
    }

    public DeliveryReturnInformation getDelivery() {
        return delivery;
    }
}
