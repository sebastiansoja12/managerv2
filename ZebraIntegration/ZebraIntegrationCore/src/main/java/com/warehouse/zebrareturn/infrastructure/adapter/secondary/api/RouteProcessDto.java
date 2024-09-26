package com.warehouse.zebrareturn.infrastructure.adapter.secondary.api;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RouteProcessDto {
    Long parcelId;
    UUID processId;
}
