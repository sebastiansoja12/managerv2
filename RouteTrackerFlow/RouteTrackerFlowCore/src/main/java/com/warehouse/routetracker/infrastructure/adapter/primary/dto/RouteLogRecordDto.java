package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RouteLogRecordDto {
    ProcessIdDto processId;
    ShipmentIdDto parcelId;
    RouteLogRecordDetailsDto routeLogRecordDetails;
    ReturnCodeDto returnCode;
    FaultDescriptionDto faultDescription;
}
