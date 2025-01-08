package com.warehouse.routelogger.dto;

import java.util.List;


public record RejectTrackerRequestDto(List<RejectTrackerRequestDetailsDto> rejectTrackerRequestDetails) {
}
