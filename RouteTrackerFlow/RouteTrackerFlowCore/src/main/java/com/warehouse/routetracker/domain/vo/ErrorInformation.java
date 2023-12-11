package com.warehouse.routetracker.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorInformation {
    Long parcelId;
    Error error;
}
