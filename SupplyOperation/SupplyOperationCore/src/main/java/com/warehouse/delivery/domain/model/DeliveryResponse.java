package com.warehouse.delivery.domain.model;

import java.util.UUID;

import com.warehouse.delivery.infrastructure.adapter.secondary.api.UpdateStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryResponse {
    private UUID id;
    private Long parcelId;
    private String deliveryStatus;
    private UpdateStatus updateStatus;

    public void updateStatus(UpdateStatus updateStatus) {
        this.updateStatus = updateStatus;
    }
}
