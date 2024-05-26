package com.warehouse.routelogger.dto;

import lombok.Value;

@Value
public class UsernameLogRequestDto {
    Long parcelId;
    String username;
    ProcessTypeDto processType;
}
