package com.warehouse.shipment.domain.port.primary;

import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentErrorCode;
import com.warehouse.shipment.domain.handler.ShipmentDefaultHandler;
import com.warehouse.shipment.domain.handler.ShipmentStatusHandler;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.port.secondary.MailServicePort;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.TrackingStatusServicePort;
import com.warehouse.shipment.domain.service.*;
import com.warehouse.shipment.domain.vo.*;


public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentService shipmentService;

    private final Logger logger;

    private final PathFinderServicePort pathFinderServicePort;

    private final NotificationCreatorProvider notificationCreatorProvider;

    private final MailServicePort mailServicePort;

    private final TrackingStatusServicePort trackingStatusServicePort;

    private final Set<ShipmentStatusHandler> shipmentStatusHandlers;

    private final CountryDetermineService countryDetermineService;

    private final PriceService priceService;

    private final CountryServiceAvailabilityService countryServiceAvailabilityService;

	public ShipmentPortImpl(final ShipmentService shipmentService,
                            final Logger logger,
                            final PathFinderServicePort pathFinderServicePort,
                            final NotificationCreatorProvider notificationCreatorProvider,
                            final MailServicePort mailServicePort,
                            final TrackingStatusServicePort trackingStatusServicePort,
                            final Set<ShipmentStatusHandler> shipmentStatusHandlers,
                            final CountryDetermineService countryDetermineService,
                            final PriceService priceService,
                            final CountryServiceAvailabilityService countryServiceAvailabilityService) {
		this.shipmentService = shipmentService;
		this.logger = logger;
		this.pathFinderServicePort = pathFinderServicePort;
		this.notificationCreatorProvider = notificationCreatorProvider;
        this.mailServicePort = mailServicePort;
        this.trackingStatusServicePort = trackingStatusServicePort;
        this.shipmentStatusHandlers = shipmentStatusHandlers;
        this.countryDetermineService = countryDetermineService;
        this.priceService = priceService;
        this.countryServiceAvailabilityService = countryServiceAvailabilityService;
    }

    @Override
    @Transactional
    public Result<ShipmentCreateResponse, ShipmentErrorCode> ship(final ShipmentCreateRequest request) {

        final Address recipientAddress = Address.from(request.getSender());
        
        final Sender sender = request.getSender();
        
        final Recipient recipient = request.getRecipient();
        
        final Country originCountry = request.getOriginCountry();

        final Country destinationCountry = request.getDestinationCountry();

		final Result<VoronoiResponse, ShipmentErrorCode> voronoiResponse = this.pathFinderServicePort
				.determineDeliveryDepartment(recipientAddress);
        
        if (voronoiResponse.isFailure()) {
            return Result.failure(voronoiResponse.getFailure());
        }

        final Price shipmentPrice = determineShipmentPrice(request);

        final ShipmentId shipmentId = this.shipmentService.nextShipmentId();

		final Shipment shipment = new Shipment(shipmentId, sender, recipient, request.getShipmentSize(), null,
				originCountry, destinationCountry, shipmentPrice.getMoney(), false,
				voronoiResponse.getSuccess().getValue(), null, request.getShipmentPriority());

        this.shipmentService.createShipment(shipment);

        logCreatedShipment(shipment);

        return Result.success(new ShipmentCreateResponse(shipmentId));
    }

    private Price determineShipmentPrice(final ShipmentCreateRequest request) {
        return request.getPrice() == null || !request.getPrice().isDefined() ?
                this.priceService.determineShipmentPrice(request.getShipmentSize(), Currency.PLN) : new Price(request.getPrice());
    }

    @Override
    @Transactional
    public Result<Void, ShipmentErrorCode> update(final ShipmentUpdateRequest request) {

        final Shipment shipment = Shipment.from(request);

        //this.pathFinderServicePort.determineDeliveryDepot(shipment, address);

        final ShipmentId shipmentId = request.getShipmentId();
        final Sender sender = request.getSender();
        final Recipient recipient = request.getRecipient();
        final ShipmentStatus shipmentStatus = shipment.getShipmentStatus();

        switch (shipmentStatus) {
            case REDIRECT -> {
                this.shipmentService.changeRecipientTo(shipmentId, recipient);
                this.shipmentService.notifyRelatedShipmentRedirected(shipmentId, this.shipmentService.nextShipmentId());
                this.trackingStatusServicePort.notifyShipmentStatusChanged(shipmentId, ShipmentStatus.REDIRECT);
            }
            case REROUTE -> {
                this.shipmentService.changeRecipientTo(shipmentId, recipient);
                this.shipmentService.changeSenderTo(shipmentId, sender);
                this.trackingStatusServicePort.notifyShipmentStatusChanged(shipmentId, ShipmentStatus.REROUTE);
            }

            default -> {
                return Result.failure(ShipmentErrorCode.SHIPMENT_202);
            }
        }
        return Result.success();
    }

    @Override
    public Result<Void, ShipmentErrorCode> addDangerousGood(final ShipmentId shipmentId, final DangerousGoodCreateRequest request) {
        if (!existsShipment(shipmentId)) {
            return Result.failure(ShipmentErrorCode.SHIPMENT_202);
        }

        final DangerousGood dangerousGood = DangerousGood.from(request);




        return Result.success();
    }

    @Override
    public void changeSenderTo(final ShipmentCreateRequest request) {
        final Shipment shipment = Shipment.from(request);
        final Sender sender = request.getSender();
        final ShipmentId shipmentId = shipment.getShipmentId();
        this.shipmentService.changeSenderTo(shipmentId, sender);
    }

    @Override
    public void changeRecipientTo(final ShipmentCreateRequest request) {
        final Shipment shipment = Shipment.from(request);
        final Recipient recipient = Recipient.from(shipment);
        final ShipmentId shipmentId = shipment.getShipmentId();
        this.shipmentService.changeRecipientTo(shipmentId, recipient);
    }

    @Override
    public void changeShipmentTypeTo(final ShipmentCreateRequest request) {
        final Shipment shipment = Shipment.from(request);
        final ShipmentType shipmentType = shipment.getShipmentType();
        final ShipmentId shipmentId = shipment.getShipmentId();
        this.shipmentService.changeShipmentTypeTo(shipmentId, shipmentType);
    }

	@Override
	public void changeShipmentStatusTo(final ShipmentStatusRequest request) {
		final ShipmentStatus status = request.getShipmentStatus();
		final ShipmentId shipmentId = request.getShipmentId();
        shipmentStatusHandlers.stream()
                .filter(shipmentStatusHandler -> shipmentStatusHandler.canHandle(status))
                .findAny()
                .ifPresentOrElse(shipmentStatusHandler ->
                                shipmentStatusHandler.notifyShipmentStatusChange(shipmentId), ShipmentDefaultHandler::new);
	}

    @Override
    public void changeShipmentSignatureTo(final SignatureChangeRequest request) {
        // change signature in service
    }

    @Override
    public void changeOriginCountryTo(final ShipmentCountryRequest request) {
        final ShipmentId shipmentId = request.shipmentId();
        final Country originCountry = request.originCountry();
        this.shipmentService.changeShipmentOriginCountryTo(shipmentId, originCountry);
    }

    @Override
    public void changeDestinationCountryTo(final ShipmentCountryRequest request) {
        final ShipmentId shipmentId = request.shipmentId();
        final Country destinationCountry = request.destinationCountry();
        this.shipmentService.changeShipmentDestinationCountryTo(shipmentId, destinationCountry);
    }

    @Override
    public void changeShipmentCountries(final ShipmentCountryRequest request) {
        this.shipmentService.changeShipmentCountries(request);
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
		logger.info("Detected service to create shipment with telephone number {0} and origin country {1} and destination country {2}",
				shipment.getSender().getTelephoneNumber(),
                shipment.getOriginCountry(),
                shipment.getDestinationCountry());
    }

    private void logNotification(final Notification notification) {
        logger.info("Email notification to {} has been sent", notification.getRecipient());
    }

    private void logCreatedShipment(final Shipment shipment) {
        logger.info("Shipment {} has been created at {} with priority {}", shipment.getShipmentId().getValue(), shipment.getCreatedAt(),
                shipment.getShipmentPriority());
    }

}
