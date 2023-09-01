package com.warehouse.delivery.domain.model;

import java.util.UUID;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Delivery {
    UUID id;
    Long parcelId;
    String depotCode;
    String supplierCode;
    DeliveryStatus deliveryStatus;
    String token;
}
