package com.warehouse.shipment.domain.service;

import java.util.UUID;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;

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
    public Shipment find(final ShipmentId shipmentId) {
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
    public void changeSender(final ShipmentId shipmentId, final Sender sender) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeSender(sender);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeRecipient(final ShipmentId shipmentId, final Recipient recipient) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeRecipient(recipient);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeShipmentType(final ShipmentId shipmentId, final ShipmentType shipmentType) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentType(shipmentType);
        shipmentRepository.update(shipment);
    }

    @Override
    public void changeShipmentStatus(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentStatus(shipmentStatus);
        shipmentRepository.update(shipment);
    }

    @Override
    public ShipmentId nextShipmentId() {
        final long randomUUIDBits = UUID.randomUUID().getLeastSignificantBits();
        return new ShipmentId(Math.abs(randomUUIDBits));
    }
}
