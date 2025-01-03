package com.warehouse.deliveryreject.infrastructure.adapter.secondary.api;

import com.warehouse.delivery.dto.DeliveryStatusDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.deliveryreject.domain.vo.RejectTrackerRequest;

public record RejectTrackerRequestDto(ShipmentIdDto shipmentId, RejectReasonDto rejectReason,
		DeliveryStatusDto deliveryStatus, ProcessTypeDto processType) {

	public static RejectTrackerRequestDto from(final RejectTrackerRequest request) {
		final ShipmentIdDto shipmentId = ShipmentIdDto.from(request.getShipmentId());
		final RejectReasonDto rejectReason = RejectReasonDto.from(request.getRejectReason());
		return new RejectTrackerRequestDto(shipmentId, rejectReason, DeliveryStatusDto.REJECTED, ProcessTypeDto.valueOf(request.getProcessType().name()));
	}
}
