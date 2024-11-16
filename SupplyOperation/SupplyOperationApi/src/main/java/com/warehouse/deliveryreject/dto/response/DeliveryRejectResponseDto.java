package com.warehouse.deliveryreject.dto.response;

import com.warehouse.deliveryreject.dto.DeviceInformationDto;
import com.warehouse.deliveryreject.dto.ShipmentIdDto;

public record DeliveryRejectResponseDto(ShipmentIdDto shipmentId, ShipmentIdDto newShipmentId,
                                        String reason, DeviceInformationDto deviceInformation) {
}
