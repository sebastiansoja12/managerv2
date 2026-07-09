package com.warehouse.shipment.domain.handler;

import static com.warehouse.commonassets.enumeration.ShipmentStatus.CREATED;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class ShipmentCreatedHandler implements ShipmentStatusHandler {

    @Override
    public boolean canHandle(final ShipmentStatus shipmentStatus) {
        return CREATED.equals(shipmentStatus);
    }

    @Override
    public void notifyShipmentStatusChange(final ShipmentId shipmentId) {
        log.warn("Shipment {} already created, status {} cannot be changed", shipmentId, CREATED);
        throw new RuntimeException("Shipment already created, status cannot be changed");
    }
}
