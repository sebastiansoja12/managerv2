package com.warehouse.routelogger.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
public class DeliveryMissedRequestDto {
    String depotCode;
    String supplierCode;
    Long parcelId;
    ProcessTypeDto processType = ProcessTypeDto.MISS;
}
