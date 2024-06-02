package com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class TerminalLogRequestDto {
    String zebraId;
    String processType;
    Long parcelId;
}
