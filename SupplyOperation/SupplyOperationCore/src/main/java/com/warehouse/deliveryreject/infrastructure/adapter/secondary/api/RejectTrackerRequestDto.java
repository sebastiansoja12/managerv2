package com.warehouse.deliveryreject.infrastructure.adapter.secondary.api;

import java.util.List;

public record RejectTrackerRequestDto(List<RejectTrackerRequestDetailsDto> rejectTrackerRequestDetails) {
}
