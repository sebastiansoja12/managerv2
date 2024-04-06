package com.warehouse.delivery.domain.model;


import com.warehouse.delivery.domain.enumeration.DeliveryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryRequest {
    private Long parcelId;
    private String depotCode;
    private String supplierCode;
    private DeliveryStatus deliveryStatus;
    private String token;

    public void updateDeliveryStatus() {
        this.deliveryStatus = DeliveryStatus.DELIVERY;
    }

    public void assignTokenToDelivery(String token) {
        this.token = token;
    }
}
