package com.warehouse.routelogger.dto;

import lombok.Value;

@Value
public class TerminalLogRequestDto {
    String terminalId;
    ProcessTypeDto processType;
    Long parcelId;
}
