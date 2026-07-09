package com.warehouse.shipment.domain.handler;

import static com.warehouse.commonassets.enumeration.ShipmentStatus.REDIRECT;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.service.ShipmentService;


@Component
public class ShipmentRedirectHandler implements ShipmentStatusHandler {

    private final ShipmentService shipmentService;

    public ShipmentRedirectHandler(final ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @Override
    public boolean canHandle(final ShipmentStatus shipmentStatus) {
        return REDIRECT.equals(shipmentStatus);
    }

    @Override
    public void notifyShipmentStatusChange(final ShipmentId shipmentId) {
        this.shipmentService.notifyRelatedShipmentRedirected(shipmentId, this.shipmentService.nextShipmentId());
    }
}
