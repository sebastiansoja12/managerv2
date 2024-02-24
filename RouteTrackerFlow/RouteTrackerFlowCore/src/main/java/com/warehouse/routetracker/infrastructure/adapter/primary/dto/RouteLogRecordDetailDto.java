package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class RouteLogRecordDetailDto {
    Long id;
    Long zebraId;
    String version;
    String username;
    String supplierCode;
    String depotCode;
    ParcelStatusDto parcelStatus;
    String description;
    LocalDateTime timestamp;
    ProcessTypeDto processType;
    String request;
}
