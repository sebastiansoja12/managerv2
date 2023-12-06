package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.model.Error;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ErrorInformation {
    Long parcelId;
    Error error;
}
