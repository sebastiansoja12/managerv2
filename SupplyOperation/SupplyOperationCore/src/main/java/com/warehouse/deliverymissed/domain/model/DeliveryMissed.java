package com.warehouse.deliverymissed.domain.model;

import java.util.UUID;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryMissed {
    private Long parcelId;
    private String depotCode;
    private String supplierCode;
    private DeliveryStatus deliveryStatus;
}
