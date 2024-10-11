package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.City;

public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final PathFinderServicePort pathFinderServicePort;

    private final Logger logger;

	public ShipmentServiceImpl(final ShipmentRepository shipmentRepository,
			final PathFinderServicePort pathFinderServicePort, final Logger logger) {
        this.shipmentRepository = shipmentRepository;
        this.pathFinderServicePort = pathFinderServicePort;
        this.logger = logger;
    }

    @Override
	public void createShipment(final Shipment shipment) {;
        shipmentRepository.save(shipment);
	}

    @Override
    public Shipment loadShipment(final ShipmentId shipmentId) {
        return shipmentRepository.findById(shipmentId);
    }

    @Override
    public void update(final ShipmentUpdate shipmentUpdate, final ShipmentId shipmentId) {

        final Shipment shipment = this.shipmentRepository.findById(shipmentId);

        final Address address = Address.from(shipmentUpdate.getRecipient());

        final City city = pathFinderServicePort.determineDeliveryDepot(address);

        shipment.updateDestination(city);

        shipmentRepository.update(shipment);
    }

    @Override
    public boolean existsShipment(final ShipmentId shipmentId) {
        return shipmentRepository.exists(shipmentId);
    }

    @Override
    public ShipmentId nextShipmentId() {
        return shipmentRepository.nextShipmentId();
    }
}
