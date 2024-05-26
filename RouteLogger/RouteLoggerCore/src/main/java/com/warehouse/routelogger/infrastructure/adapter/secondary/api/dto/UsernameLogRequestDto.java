package com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class UsernameLogRequestDto {
    Long parcelId;
    String username;
    String processType;
}
