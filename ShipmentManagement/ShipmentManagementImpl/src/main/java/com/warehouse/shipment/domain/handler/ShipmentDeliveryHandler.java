package com.warehouse.shipment.domain.handler;

import static com.warehouse.commonassets.enumeration.ShipmentStatus.DELIVERY;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.service.ShipmentService;


@Component
public class ShipmentDeliveryHandler implements ShipmentStatusHandler {

    private final ShipmentService shipmentService;

    public ShipmentDeliveryHandler(final ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @Override
    public boolean canHandle(final ShipmentStatus shipmentStatus) {
        return DELIVERY.equals(shipmentStatus);
    }

    @Override
    public void notifyShipmentStatusChange(final ShipmentId shipmentId) {
        this.shipmentService.notifyShipmentDelivered(shipmentId);
    }
}
