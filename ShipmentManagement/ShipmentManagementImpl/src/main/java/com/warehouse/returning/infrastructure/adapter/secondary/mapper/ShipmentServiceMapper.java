package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.domain.model.ReturnableShipment;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;

public class ShipmentServiceMapper {

    public ReturnableShipment map(final ShipmentDetailsDto shipment) {
        return new ReturnableShipment(shipment.shipmentId(), shipment.status(), shipment.relatedShipmentId());
    }
}
