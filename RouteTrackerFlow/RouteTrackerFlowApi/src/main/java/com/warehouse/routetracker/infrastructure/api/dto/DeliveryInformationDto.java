package com.warehouse.routetracker.infrastructure.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class DeliveryInformationDto {

    Long parcelId;
    String depotCode;
    String supplierCode;
    String deliveryStatus;
    String token;

}
