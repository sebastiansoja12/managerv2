package com.warehouse.routelogger.infrastructure.adapter.secondary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TerminalRequestDto {
    String request;
    Long parcelId;
    String processType;
}
