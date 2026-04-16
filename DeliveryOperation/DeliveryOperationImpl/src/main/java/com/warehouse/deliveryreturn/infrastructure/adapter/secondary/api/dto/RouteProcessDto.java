package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

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
