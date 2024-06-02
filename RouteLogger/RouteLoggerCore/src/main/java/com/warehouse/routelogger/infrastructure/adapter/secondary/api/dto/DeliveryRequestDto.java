package com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class DeliveryRequestDto {
    String depotCode;
    String supplierCode;
    Long parcelId;
    String processType;
}
