package com.warehouse.routelogger.dto;


public record RejectTrackerRequestDetailsDto(ShipmentIdDto shipmentId, RejectReasonDto rejectReason,
                                             String deliveryStatus, ProcessTypeDto processType) {
}
