package com.warehouse.delivery.domain.model;


import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.enumeration.DeliveryStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryRequest {
    private Long parcelId;
    private String depotCode;
    private String supplierCode;
    private DeliveryStatus deliveryStatus;
    private String token;
    private ProcessType processType;

    public void updateDeliveryStatus() {
        this.deliveryStatus = DeliveryStatus.DELIVERY;
    }

    public void assignTokenToDelivery(String token) {
        this.token = token;
    }
}
