package com.warehouse.routetracker.infrastructure.api.dto;

import lombok.Value;

@Value
public class DeliveryInformationDto {

    Long parcelId;
    String depotCode;
    String supplierCode;
    String deliveryStatus;
    String token;

}
