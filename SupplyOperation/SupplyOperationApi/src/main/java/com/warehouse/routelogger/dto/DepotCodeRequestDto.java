package com.warehouse.routelogger.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DepotCodeRequestDto {
    String depotCode;
    Long parcelId;
    String processType;
}
