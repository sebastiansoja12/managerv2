package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.model.ProcessType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReturnTrackRequest {
    Long parcelId;
    ProcessType processType;
    String username;
    String depotCode;
}
