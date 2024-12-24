package com.warehouse.shipment.domain.port.primary;

import static com.warehouse.shipment.domain.enumeration.ShipmentUpdateType.REDIRECT;
import static com.warehouse.shipment.domain.enumeration.ShipmentUpdateType.REROUTE;
import static com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes.SHIPMENT_202;

import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.ShipmentUpdateType;
import com.warehouse.shipment.domain.exception.DestinationDepartmentDeterminationException;
import com.warehouse.shipment.domain.exception.ShipmentEmptyRequestException;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentExceptionCodes;
import com.warehouse.shipment.domain.handler.ShipmentStatusHandler;
import com.warehouse.shipment.domain.model.Notification;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.SignatureChangeRequest;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.port.secondary.MailServicePort;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.TrackingStatusServicePort;
import com.warehouse.shipment.domain.service.NotificationCreatorProvider;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.domain.vo.*;


public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentService shipmentService;

    private final Logger logger;

    private final PathFinderServicePort pathFinderServicePort;

    private final NotificationCreatorProvider notificationCreatorProvider;

    private final MailServicePort mailServicePort;

    private final TrackingStatusServicePort trackingStatusServicePort;

    private final Set<ShipmentStatusHandler> shipmentStatusHandlers;

	public ShipmentPortImpl(final ShipmentService shipmentService, final Logger logger,
                            final PathFinderServicePort pathFinderServicePort,
                            final NotificationCreatorProvider notificationCreatorProvider,
                            final MailServicePort mailServicePort,
                            final TrackingStatusServicePort trackingStatusServicePort,
                            final Set<ShipmentStatusHandler> shipmentStatusHandlers) {
		this.shipmentService = shipmentService;
		this.logger = logger;
		this.pathFinderServicePort = pathFinderServicePort;
		this.notificationCreatorProvider = notificationCreatorProvider;
		this.mailServicePort = mailServicePort;
        this.trackingStatusServicePort = trackingStatusServicePort;
        this.shipmentStatusHandlers = shipmentStatusHandlers;
    }

    @Override
    public ShipmentResponse ship(final ShipmentRequest request) {

        final Shipment shipment = Shipment.from(request);

        if (ObjectUtils.isEmpty(shipment)) {
            throw new ShipmentEmptyRequestException(ShipmentExceptionCodes.SHIPMENT_204);
        }

        logShipment(shipment);

        final Address address = Address.from(shipment.getRecipient());

        shipment.prepareShipmentToCreate();

        final City city = this.pathFinderServicePort.determineDeliveryDepot(address);

        if (Objects.isNull(city) || StringUtils.isEmpty(city.getValue())) {
            throw new DestinationDepartmentDeterminationException(SHIPMENT_202);
        }

        shipment.updateDestination(city);

        final ShipmentId shipmentId = this.shipmentService.nextShipmentId();

        shipment.setShipmentId(shipmentId);

        this.shipmentService.createShipment(shipment);

        logCreatedShipment(shipment);

        final RouteProcess routeProcess = this.shipmentService.initializeRouteProcess(shipmentId);

        return new ShipmentResponse(routeProcess.getProcessId().toString(), routeProcess.getShipmentId());
    }

    @Override
    public void update(final ShipmentUpdateRequest request) {

        final Address address = Address.from(request.getRecipient());

        final City city = this.pathFinderServicePort.determineDeliveryDepot(address);

        request.updateDestination(city);

        final ShipmentId shipmentId = request.getShipmentId();
        final Sender sender = request.getSender();
        final Recipient recipient = request.getRecipient();
        final ShipmentUpdateType shipmentUpdateType = request.getShipmentUpdateType();

        if (REDIRECT.equals(shipmentUpdateType)) {
            this.shipmentService.changeRecipientTo(shipmentId, recipient);
            this.shipmentService.notifyRelatedShipmentRedirected(shipmentId, this.shipmentService.nextShipmentId());
            this.trackingStatusServicePort.notifyShipmentStatusChanged(shipmentId, ShipmentStatus.REDIRECT);
        } else if (REROUTE.equals(shipmentUpdateType)) {
            this.shipmentService.changeRecipientTo(shipmentId, recipient);
            this.shipmentService.changeSenderTo(shipmentId, sender);
            this.shipmentService.notifyShipmentRerouted(shipmentId);
            this.trackingStatusServicePort.notifyShipmentStatusChanged(shipmentId, ShipmentStatus.REROUTE);
        }
    }

    @Override
    public void changeSenderTo(final ShipmentRequest request) {
        final Shipment shipment = Shipment.from(request);
        final Sender sender = Sender.from(shipment);
        final ShipmentId shipmentId = shipment.getShipmentId();
        this.shipmentService.changeSenderTo(shipmentId, sender);
    }

    @Override
    public void changeRecipientTo(final ShipmentRequest request) {
        final Shipment shipment = Shipment.from(request);
        final Recipient recipient = Recipient.from(shipment);
        final ShipmentId shipmentId = shipment.getShipmentId();
        this.shipmentService.changeRecipientTo(shipmentId, recipient);
    }

    @Override
    public void changeShipmentTypeTo(final ShipmentRequest request) {
        final Shipment shipment = Shipment.from(request);
        final ShipmentType shipmentType = shipment.getShipmentType();
        final ShipmentId shipmentId = shipment.getShipmentId();
        this.shipmentService.changeShipmentTypeTo(shipmentId, shipmentType);
    }

	@Override
	public void changeShipmentStatusTo(final ShipmentStatusRequest request) {
		final ShipmentStatus status = request.getShipmentStatus();
		final ShipmentId shipmentId = request.getShipmentId();
        for (final ShipmentStatusHandler shipmentStatusHandler : shipmentStatusHandlers) {
            if (shipmentStatusHandler.canHandle(status)) {
                shipmentStatusHandler.notifyShipmentStatusChange(shipmentId);
            }
        }
//        shipmentStatusHandlers.stream()
//                .filter(shipmentStatusHandler -> shipmentStatusHandler.canHandle(status))
//                .findAny()
//                .ifPresentOrElse(shipmentStatusHandler ->
//                                shipmentStatusHandler.notifyShipmentStatusChange(shipmentId), ShipmentDefaultHandler::new);
	}

    @Override
    public void changeShipmentSignatureTo(final SignatureChangeRequest request) {
        // change signature in service
    }

    @Override
    public Shipment loadShipment(final ShipmentId shipmentId) {
        return this.shipmentService.find(shipmentId);
    }

    @Override
    public boolean existsShipment(final ShipmentId shipmentId) {
        return this.shipmentService.existsShipment(shipmentId);
    }

    private void logShipment(final Shipment shipment) {
		logger.info("Detected service to create shipment with telephone number {}",
				shipment.getSender().getTelephoneNumber());
    }

    private void logNotification(final Notification notification) {
        logger.info("Email notification to {} has been sent", notification.getRecipient());
    }

    private void logCreatedShipment(final Shipment shipment) {
        logger.info("Shipment {} has been created at {}", shipment.getShipmentId(), shipment.getCreatedAt());
    }

}
