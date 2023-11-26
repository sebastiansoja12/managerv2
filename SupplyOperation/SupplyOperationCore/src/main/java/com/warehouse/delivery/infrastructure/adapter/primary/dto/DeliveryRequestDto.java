package com.warehouse.delivery.infrastructure.adapter.primary.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class DeliveryRequestDto {
    Long parcelId;
    String depotCode;
    String supplierCode;
}
