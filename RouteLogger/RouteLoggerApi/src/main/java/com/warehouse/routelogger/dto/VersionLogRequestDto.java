package com.warehouse.routelogger.dto;

import lombok.Value;

@Value
public class VersionLogRequestDto {
    Long parcelId;
    ProcessTypeDto processType;
    String version;
}
