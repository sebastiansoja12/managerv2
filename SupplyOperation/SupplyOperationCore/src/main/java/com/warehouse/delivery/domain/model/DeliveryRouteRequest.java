package com.warehouse.delivery.domain.model;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DeliveryRouteRequest {
    UUID id;
    Long parcelId;
    String depotCode;
    String supplierCode;
    DeliveryStatus deliveryStatus;
    String token;
}
