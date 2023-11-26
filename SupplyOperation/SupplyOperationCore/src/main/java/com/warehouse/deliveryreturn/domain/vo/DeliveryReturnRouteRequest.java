package com.warehouse.deliveryreturn.domain.vo;

import java.util.UUID;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryReturnRouteRequest {
    UUID id;
    Long parcelId;
    String depotCode;
    String supplierCode;
    DeliveryStatus deliveryStatus;
    String token;
}
