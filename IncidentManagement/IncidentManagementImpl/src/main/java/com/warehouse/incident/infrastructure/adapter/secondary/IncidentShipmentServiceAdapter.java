package com.warehouse.incident.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.incident.application.port.secondary.IncidentShipmentServicePort;
import com.warehouse.incident.domain.vo.IncidentShipment;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;

public class IncidentShipmentServiceAdapter implements IncidentShipmentServicePort {

    private final ShipmentApiService shipmentApiService;

    public IncidentShipmentServiceAdapter(final ShipmentApiService shipmentApiService) {
        this.shipmentApiService = shipmentApiService;
    }

    @Override
    public IncidentShipment getShipment(final ShipmentId shipmentId) {
        final ShipmentDetailsDto shipment = this.shipmentApiService.getShipment(shipmentId);
        return new IncidentShipment(shipment.shipmentId(),
                shipment.trackingNumber() == null ? null : new TrackingNumber(shipment.trackingNumber()));
    }
}
