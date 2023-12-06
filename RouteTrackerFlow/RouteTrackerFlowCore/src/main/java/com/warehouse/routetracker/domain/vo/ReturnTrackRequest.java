package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.model.ProcessType;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class ReturnTrackRequest {
    Long parcelId;
    ProcessType processType;
    String username;
    String depotCode;
}
