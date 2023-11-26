package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.enumeration.Status;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeliveryInformation {
    Long parcelId;
    String depotCode;
    String supplierCode;
    String token;
    Status deliveryStatus;
}
