package com.warehouse.shipment.domain.port.primary;

import static com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes.SHIPMENT_202;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.DestinationDepotDeterminationException;
import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes;
import com.warehouse.shipment.domain.model.Notification;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.port.secondary.MailServicePort;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.service.NotificationCreatorProvider;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.vo.*;


public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentService shipmentService;

    private final Logger logger;

    private final PathFinderServicePort pathFinderServicePort;

    private final NotificationCreatorProvider notificationCreatorProvider;

    private final MailServicePort mailServicePort;

    private final RouteLogServicePort routeLogServicePort;

	public ShipmentPortImpl(final ShipmentService shipmentService, final Logger logger,
                            final PathFinderServicePort pathFinderServicePort,
                            final NotificationCreatorProvider notificationCreatorProvider, final MailServicePort mailServicePort,
                            final RouteLogServicePort routeLogServicePort) {
		this.shipmentService = shipmentService;
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

        final City city = this.pathFinderServicePort.determineDeliveryDepot(address);

        if (Objects.isNull(city) || city.getValue() == null) {
            throw new DestinationDepotDeterminationException(SHIPMENT_202);
        }

        shipment.updateDestination(city);

        final ShipmentId shipmentId = this.shipmentService.nextShipmentId();

        shipment.setShipmentId(shipmentId);

        this.shipmentService.createShipment(shipment);

        logCreatedShipment(shipment);

        final RouteProcess routeProcess = this.routeLogServicePort.initializeRouteProcess(shipmentId);

        return new ShipmentResponse(routeProcess.getProcessId().toString(), routeProcess.getShipmentId());
    }

    @Override
    public void update(final ShipmentUpdateRequest request) {

        final Address address = Address.from(request.getRecipient());

        final City city = this.pathFinderServicePort.determineDeliveryDepot(address);

        request.updateDestination(city);

        shipmentService.updateShipment(request.getShipmentUpdate(), request.getShipmentId());
    }

    @Override
    public Shipment loadParcel(final ShipmentId shipmentId) {
        return this.shipmentService.loadShipment(shipmentId);
    }

    @Override
    public boolean existsShipment(final ShipmentId shipmentId) {
        return this.shipmentService.existsShipment(shipmentId);
    }


    private Shipment extractShipmentFromRequest(final ShipmentRequest request) {
        return request.getShipment();
    }

    private void logShipment(final Shipment parcel) {
		logger.info("Detected service to create shipment with telephone number {}",
				parcel.getSender().getTelephoneNumber());
    }

    private void logNotification(final Notification notification) {
        logger.info("Email notification to {} has been sent", notification.getRecipient());
    }

    private void logCreatedShipment(final Shipment shipment) {
        logger.info("Shipment {} has been created at {}", shipment.getShipmentId(), shipment.getCreatedAt());
    }

}
