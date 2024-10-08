package com.warehouse.shipment.domain.service;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.model.Notification;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.City;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.vo.ShipmentUpdateResponse;

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
	public Parcel createShipment(final Shipment shipment) {
        return shipmentRepository.save(shipment);
	}

    @Override
    public Parcel loadParcel(final ShipmentId shipmentId) {
        return shipmentRepository.findParcelById(shipmentId);
    }

    @Override
    public ShipmentUpdateResponse update(final ShipmentUpdate shipmentUpdate) {

        final Address address = Address.from(shipmentUpdate);

        final City city = pathFinderServicePort.determineDeliveryDepot(address);

        if (!city.getValue().equals(shipmentUpdate.getDestination())) {
            updateParcelDestinationForReroute(shipmentUpdate, city);
        }

        final Parcel parcel = shipmentRepository.update(shipmentUpdate);

        return new ShipmentUpdateResponse(parcel);
    }

    @Override
    public boolean exists(final ShipmentId shipmentId) {
        return shipmentRepository.exists(shipmentId);
    }

    private void updateParcelDestinationForReroute(final ShipmentUpdate shipmentUpdate, final City city) {
        if (ObjectUtils.isNotEmpty(city) && city.getValue() != null) {
            shipmentUpdate.updateDestination(city.getValue());
        }
    }

    private void logNotification(final Notification notification) {
        logger.info("Email notification to {} has been sent", notification.getRecipient());
    }

    private void logParcel(final Parcel parcel) {
        logger.info("Shipment {} has been created at {}", parcel.getShipmentId(), parcel.getCreatedAt());
    }
}
