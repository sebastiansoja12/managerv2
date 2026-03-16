package com.warehouse.shipment.domain.handler;

import static com.warehouse.commonassets.enumeration.ShipmentStatus.RETURN;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.service.ShipmentService;


@Component
public class ShipmentReturnHandler implements ShipmentStatusHandler {

    private final ShipmentService shipmentService;

    public ShipmentReturnHandler(final ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @Override
    public boolean canHandle(final ShipmentStatus shipmentStatus) {
        return RETURN.equals(shipmentStatus);
    }

    @Override
    public void notifyShipmentStatusChange(final ShipmentId shipmentId) {
        this.shipmentService.notifyShipmentReturned(shipmentId);
    }
}
