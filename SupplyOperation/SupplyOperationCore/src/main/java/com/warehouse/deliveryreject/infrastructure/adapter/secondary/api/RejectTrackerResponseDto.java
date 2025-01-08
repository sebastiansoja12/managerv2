package com.warehouse.deliveryreject.infrastructure.adapter.secondary.api;

import java.util.List;

public record RejectTrackerResponseDto(List<RejectTrackerResponseDetailsDto> rejectTrackerResponseDetails) {
}
