package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.model.ProcessType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ReturnTrackRequest {
    Long id;
    String request;
    LocalDateTime timestamp;
    ProcessType processType;
    String username;
    String depotCode;
}
