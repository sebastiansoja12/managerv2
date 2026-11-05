package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.logistics.application.port.secondary.ShipmentAcceptServicePort;
import com.warehouse.shipment.infrastructure.ShipmentApiService;

public class ShipmentAcceptServiceAdapter implements ShipmentAcceptServicePort {

    private final ShipmentApiService shipmentApiService;

    public ShipmentAcceptServiceAdapter(final ShipmentApiService shipmentApiService) {
        this.shipmentApiService = shipmentApiService;
    }

    @Override
    public ShipmentId acceptInDepartment(final TrackingNumber trackingNumber) {
        return shipmentApiService.acceptShipmentInDepartment(trackingNumber).shipmentId();
    }
}
