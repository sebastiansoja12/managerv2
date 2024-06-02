package com.warehouse.routelogger.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RequestDto {
    String request;
    Long parcelId;
    ProcessTypeDto processType;
}
