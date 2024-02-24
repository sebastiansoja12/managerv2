package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class RouteProcessDto {
    Long parcelId;
    UUID processId;
}
