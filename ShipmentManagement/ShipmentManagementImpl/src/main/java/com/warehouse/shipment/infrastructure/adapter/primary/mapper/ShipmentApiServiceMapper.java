package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;

public class ShipmentApiServiceMapper {

    public ShipmentDetailsDto map(final ShipmentResult shipment) {
        return new ShipmentDetailsDto(
                shipment.snapshot().shipmentId(),
                shipment.snapshot().shipmentStatus(),
                shipment.snapshot().shipmentRelatedId()
        );
    }
}
