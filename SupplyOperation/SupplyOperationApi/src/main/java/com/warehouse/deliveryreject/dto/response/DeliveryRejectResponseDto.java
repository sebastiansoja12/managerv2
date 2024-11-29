package com.warehouse.deliveryreject.dto.response;

import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.delivery.dto.ShipmentIdDto;
import com.warehouse.deliveryreject.dto.RejectReasonDto;

public record DeliveryRejectResponseDto(ShipmentIdDto shipmentId, ShipmentIdDto newShipmentId,
                                        RejectReasonDto rejectReason, DeviceInformationDto deviceInformation) {
}
