package com.warehouse.shipment.domain.service;

import java.util.UUID;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;

public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

	public ShipmentServiceImpl(final ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Override
	public void createShipment(final Shipment shipment) {
        shipmentRepository.save(shipment);
	}

    @Override
    public Shipment loadShipment(final ShipmentId shipmentId) {
        return shipmentRepository.findById(shipmentId);
    }

    @Override
    public void updateShipment(final ShipmentUpdate shipmentUpdate, final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.update(shipmentUpdate);
        shipmentRepository.update(shipment);
    }

    @Override
    public boolean existsShipment(final ShipmentId shipmentId) {
        return shipmentRepository.exists(shipmentId);
    }

    @Override
    public ShipmentId nextShipmentId() {
        final long randomUUIDBits = UUID.randomUUID().getLeastSignificantBits();
        return new ShipmentId(Math.abs(randomUUIDBits));
    }
}
