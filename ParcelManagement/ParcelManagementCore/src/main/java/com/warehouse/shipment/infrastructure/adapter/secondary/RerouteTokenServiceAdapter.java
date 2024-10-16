package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.port.secondary.RerouteTokenServicePort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RerouteTokenServiceAdapter implements RerouteTokenServicePort {


    @Override
    public void notifyShipmentStatusChanged(final ShipmentId shipmentId) {

    }
}
