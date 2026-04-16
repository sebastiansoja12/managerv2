package com.warehouse.shipment.domain.handler;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import org.springframework.stereotype.Component;

@Component
public class ShipmentDefaultHandler implements ShipmentStatusHandler {

    @Override
    public boolean canHandle(final ShipmentStatus shipmentStatus) {
        return false;
    }

    @Override
    public void notifyShipmentStatusChange(final ShipmentId shipmentId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
