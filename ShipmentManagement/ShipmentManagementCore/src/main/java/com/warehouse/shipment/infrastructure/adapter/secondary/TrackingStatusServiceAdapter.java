package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.port.secondary.TrackingStatusServicePort;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentStatusDto;
import com.warehouse.tracking.ShipmentStatusChanged;
import com.warehouse.tracking.TrackingStatusEventPublisher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrackingStatusServiceAdapter implements TrackingStatusServicePort {

    private final TrackingStatusEventPublisher trackingStatusEventPublisher;

    public TrackingStatusServiceAdapter(final TrackingStatusEventPublisher trackingStatusEventPublisher) {
        this.trackingStatusEventPublisher = trackingStatusEventPublisher;
    }

    @Override
    public void notifyShipmentStatusChanged(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus) {
        this.trackingStatusEventPublisher.send(new ShipmentStatusChanged(ShipmentIdDto.from(shipmentId),
                ShipmentStatusDto.from(shipmentStatus)));
    }
}
