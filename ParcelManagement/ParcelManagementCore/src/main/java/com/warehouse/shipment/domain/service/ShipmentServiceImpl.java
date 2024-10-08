package com.warehouse.shipment.domain.service;

import static com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes.SHIPMENT_202;

import java.util.Objects;

import com.warehouse.shipment.domain.model.Shipment;
import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.DestinationDepotDeterminationException;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.model.Notification;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.vo.*;

public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final PathFinderServicePort pathFinderServicePort;

    private final NotificationCreatorProvider notificationCreatorProvider;
    
    private final MailServicePort mailServicePort;

    private final Logger logger;

    private final RouteLogServicePort routeLogServicePort;

	public ShipmentServiceImpl(final ShipmentRepository shipmentRepository,
			final PathFinderServicePort pathFinderServicePort,
			final NotificationCreatorProvider notificationCreatorProvider, final MailServicePort mailServicePort,
			final Logger logger, final RouteLogServicePort routeLogServicePort) {
        this.shipmentRepository = shipmentRepository;
        this.pathFinderServicePort = pathFinderServicePort;
        this.notificationCreatorProvider = notificationCreatorProvider;
        this.mailServicePort = mailServicePort;
        this.logger = logger;
        this.routeLogServicePort = routeLogServicePort;
    }

    @Override
	public ShipmentResponse createShipment(final Shipment shipment) {

        final Address address = Address.from(shipment.getRecipient());

		final City city = pathFinderServicePort.determineDeliveryDepot(address);

        if (Objects.isNull(city) || city.getValue() == null) {
            throw new DestinationDepotDeterminationException(SHIPMENT_202);
		}

        shipment.updateDestination(city.getValue());

        final Parcel parcel = shipmentRepository.save(shipment);

        logParcel(parcel);

        // sendNotification(parcel);

        final RouteProcess routeProcess = routeLogServicePort.initializeRouteProcess(parcel.getId());

        return new ShipmentResponse(routeProcess.getProcessId().toString(), routeProcess.getParcelId().getId());
	}

    private void sendNotification(Parcel parcel) {
        final Notification notification = notificationCreatorProvider.createNotification(parcel);

        mailServicePort.sendShipmentNotification(notification);

        logNotification(notification);
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

    private void updateParcelDestinationForReroute(ShipmentUpdate shipmentUpdate, City city) {
        if (ObjectUtils.isNotEmpty(city) && city.getValue() != null) {
            shipmentUpdate.updateDestination(city.getValue());
        }
    }

    private void logNotification(Notification notification) {
        logger.info("Email notification to {} has been sent", notification.getRecipient());
    }

    private void logParcel(Parcel parcel) {
        logger.info("Parcel {} has been created at {}", parcel.getId(), parcel.getCreatedAt());
    }
}
