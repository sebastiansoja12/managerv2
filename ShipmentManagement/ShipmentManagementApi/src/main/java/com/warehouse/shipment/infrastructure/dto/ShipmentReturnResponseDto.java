package com.warehouse.shipment.infrastructure.dto;

import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentReturnResponseDto(ShipmentId shipmentId,
                                        ShipmentId newShipmentId,
                                        String newTrackingNumber) {
}
