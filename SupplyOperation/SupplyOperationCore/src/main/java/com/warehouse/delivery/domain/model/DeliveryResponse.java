package com.warehouse.delivery.domain.model;

import com.warehouse.delivery.infrastructure.adapter.secondary.api.UpdateStatus;


public class DeliveryResponse {
    private Long parcelId;
    private String deliveryStatus;
    private UpdateStatus updateStatus;

    public void updateStatus(UpdateStatus updateStatus) {
        this.updateStatus = updateStatus;
    }
}
