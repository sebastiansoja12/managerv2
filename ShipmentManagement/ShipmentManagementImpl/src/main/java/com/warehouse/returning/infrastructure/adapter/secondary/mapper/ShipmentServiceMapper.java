package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.returning.domain.model.ReturnableShipment;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;

public class ShipmentServiceMapper {

    public ReturnableShipment map(final ShipmentDetailsDto shipment) {
        return new ReturnableShipment(shipment.shipmentId(),
                shipment.trackingNumber() == null ? null : new TrackingNumber(shipment.trackingNumber()),
                shipment.status(), shipment.relatedShipmentId());
    }
}
