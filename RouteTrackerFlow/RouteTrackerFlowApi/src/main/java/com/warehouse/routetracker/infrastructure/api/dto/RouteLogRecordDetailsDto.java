package com.warehouse.routetracker.infrastructure.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Value
@Builder
@Jacksonized
public class RouteLogRecordDetailsDto {
    Set<RouteLogRecordDetailDto> routeLogRecordDetailSet;
}
