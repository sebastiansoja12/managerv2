package com.warehouse.logistics.domain.model;

import java.util.UUID;

import com.warehouse.logistics.domain.enumeration.DeliveryStatus;

import com.warehouse.logistics.infrastructure.adapter.secondary.api.UpdateStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Delivery {
    private UUID id;
    private Long parcelId;
    private String depotCode;
    private String supplierCode;
    private DeliveryStatus deliveryStatus;
    private String token;
    private UpdateStatus updateStatus;

    public void updateStatus(UpdateStatus updateStatus) {
        this.updateStatus = updateStatus;
    }
}
