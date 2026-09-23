package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.application.port.secondary.ShipmentServicePort;
import com.warehouse.returning.domain.model.ReturnableShipment;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ShipmentServiceMapper;
import com.warehouse.shipment.infrastructure.ShipmentApiService;

public class ShipmentServiceAdapter implements ShipmentServicePort {

    private final ShipmentApiService shipmentApiService;
    private final ShipmentServiceMapper mapper;

    public ShipmentServiceAdapter(final ShipmentApiService shipmentApiService,
                                  final ShipmentServiceMapper mapper) {
        this.shipmentApiService = shipmentApiService;
        this.mapper = mapper;
    }

    @Override
    public ReturnableShipment getShipment(final ShipmentId shipmentId) {
        return mapper.map(shipmentApiService.getShipment(shipmentId));
    }
    @Override
    public void markReturned(final ShipmentId shipmentId) {
        shipmentApiService.markReturned(shipmentId);
    }

    @Override
    public void restoreAfterReturnCancellation(final ShipmentId shipmentId) {
        shipmentApiService.restoreAfterReturnCancellation(shipmentId);
    }

    @Override
    public void completeReturn(final ShipmentId shipmentId) {
        shipmentApiService.completeReturn(shipmentId);
    }

    @Override
    public void cancelReturnShipment(final ShipmentId shipmentId) {
        shipmentApiService.cancelReturnShipment(shipmentId);
    }
}
