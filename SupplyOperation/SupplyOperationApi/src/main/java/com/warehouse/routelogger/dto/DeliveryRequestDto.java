package com.warehouse.routelogger.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
public class DeliveryRequestDto {
    String depotCode;
    String supplierCode;
    Long parcelId;
    ProcessTypeDto processType;
}
