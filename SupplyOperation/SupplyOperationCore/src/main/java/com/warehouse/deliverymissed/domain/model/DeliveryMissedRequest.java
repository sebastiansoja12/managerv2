package com.warehouse.deliverymissed.domain.model;

import com.warehouse.deliverymissed.domain.enumeration.DeliveryStatus;
import lombok.Data;

@Data
public class DeliveryMissedRequest {
    private Long parcelId;
    private String depotCode;
    private String supplierCode;
    private DeliveryStatus deliveryStatus = DeliveryStatus.UNAVAILABLE;
}
