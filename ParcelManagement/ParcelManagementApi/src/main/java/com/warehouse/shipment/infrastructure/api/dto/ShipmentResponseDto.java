package com.warehouse.shipment.infrastructure.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ShipmentResponseDto {
    String routeProcessId;
    Long parcelId;
}
