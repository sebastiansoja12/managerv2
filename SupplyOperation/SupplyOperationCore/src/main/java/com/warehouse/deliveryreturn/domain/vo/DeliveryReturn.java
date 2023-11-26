package com.warehouse.deliveryreturn.domain.vo;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class DeliveryReturn {
    UUID id;
    Long parcelId;
    String depotCode;
    String supplierCode;
    String deliveryStatus;
    String token;
}
