package com.warehouse.routetracker.infrastructure.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RouteLogRecordDto {
    ProcessIdDto processId;
    ParcelIdDto parcelId;
    RouteLogRecordDetailsDto routeLogRecordDetails;
    ReturnCodeDto returnCode;
    FaultDescriptionDto faultDescription;
}
