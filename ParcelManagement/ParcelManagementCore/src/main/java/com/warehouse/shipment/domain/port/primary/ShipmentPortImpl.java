package com.warehouse.shipment.domain.port.primary;

import com.warehouse.shipment.domain.exception.DestinationDepotDeterminationException;
import com.warehouse.shipment.domain.model.Notification;
import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.port.secondary.MailServicePort;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.service.NotificationCreatorProvider;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.vo.*;

import java.util.Objects;

import static com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes.SHIPMENT_202;


public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentService service;

    private final Logger logger;

    private final PathFinderServicePort pathFinderServicePort;

    private final NotificationCreatorProvider notificationCreatorProvider;

    private final MailServicePort mailServicePort;

    private final RouteLogServicePort routeLogServicePort;

	public ShipmentPortImpl(final ShipmentService service, final Logger logger,
			final PathFinderServicePort pathFinderServicePort,
			final NotificationCreatorProvider notificationCreatorProvider, final MailServicePort mailServicePort,
			final RouteLogServicePort routeLogServicePort) {
		this.service = service;
		this.logger = logger;
		this.pathFinderServicePort = pathFinderServicePort;
		this.notificationCreatorProvider = notificationCreatorProvider;
		this.mailServicePort = mailServicePort;
		this.routeLogServicePort = routeLogServicePort;
	}

    @Override
    public ShipmentResponse ship(final ShipmentRequest request) {
        final Shipment shipment = extractShipmentFromRequest(request);

        if (ObjectUtils.isEmpty(shipment)) {
            throw new ParcelNotFoundException(ShipmentExceptionCodes.SHIPMENT_204);
        }

        logShipment(shipment);

        final Address address = Address.from(shipment.getRecipient());

        shipment.prepareShipmentToCreate();

        final City city = pathFinderServicePort.determineDeliveryDepot(address);

        if (Objects.isNull(city) || city.getValue() == null) {
            throw new DestinationDepotDeterminationException(SHIPMENT_202);
        }

        shipment.updateDestination(city.getValue());

        final Parcel parcel = service.createShipment(shipment);

        logParcel(parcel);

        final RouteProcess routeProcess = routeLogServicePort.initializeRouteProcess(parcel.getShipmentId());

        return new ShipmentResponse(routeProcess.getProcessId().toString(), routeProcess.getParcelId().getValue());
    }

    @Override
    public ShipmentUpdateResponse update(final ShipmentUpdateRequest request) {
        final ShipmentUpdate shipmentUpdate = ShipmentUpdate.from(request);

        return service.update(shipmentUpdate);
    }

    @Override
    public Parcel loadParcel(final ShipmentId shipmentId) {
        return service.loadParcel(shipmentId);
    }

    @Override
    public boolean exists(final ShipmentId shipmentId) {
        return service.exists(shipmentId);
    }


    private Shipment extractShipmentFromRequest(final ShipmentRequest request) {
        return request.getShipment();
    }

    private void logShipment(final Shipment parcel) {
		logger.info("Detected service to create shipment with telephone number {}",
				parcel.getSender().telephoneNumber());
    }

    private void logNotification(final Notification notification) {
        logger.info("Email notification to {} has been sent", notification.getRecipient());
    }

    private void logParcel(final Parcel parcel) {
        logger.info("Shipment {} has been created at {}", parcel.getShipmentId(), parcel.getCreatedAt());
    }

}
