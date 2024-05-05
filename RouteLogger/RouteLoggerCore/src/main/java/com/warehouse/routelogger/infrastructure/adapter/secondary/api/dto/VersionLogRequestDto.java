package com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class VersionLogRequestDto {
    Long parcelId;
    String processType;
    String version;
}
