package com.warehouse.shipment.domain.port.primary;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.enumeration.PersonType;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.handler.ShipmentDefaultHandler;
import com.warehouse.shipment.domain.handler.ShipmentStatusHandler;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.TrackingStatusServicePort;
import com.warehouse.shipment.domain.service.*;
import com.warehouse.shipment.domain.vo.*;


public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentService shipmentService;

    private final Logger logger;

    private final PathFinderServicePort pathFinderServicePort;

    private final NotificationCreatorProvider notificationCreatorProvider;

    private final TrackingStatusServicePort trackingStatusServicePort;

    private final Set<ShipmentStatusHandler> shipmentStatusHandlers;

    private final CountryDetermineService countryDetermineService;

    private final PriceService priceService;

    private final CountryServiceAvailabilityService countryServiceAvailabilityService;

    private final SignatureService signatureService;

    private final ShipmentStateValidatorService shipmentStateValidatorService = new ShipmentStateValidatorServiceImpl();

	public ShipmentPortImpl(final ShipmentService shipmentService,
                            final Logger logger,
                            final PathFinderServicePort pathFinderServicePort,
                            final NotificationCreatorProvider notificationCreatorProvider,
                            final TrackingStatusServicePort trackingStatusServicePort,
                            final Set<ShipmentStatusHandler> shipmentStatusHandlers,
                            final CountryDetermineService countryDetermineService,
                            final PriceService priceService,
                            final CountryServiceAvailabilityService countryServiceAvailabilityService,
                            final SignatureService signatureService) {
		this.shipmentService = shipmentService;
		this.logger = logger;
		this.pathFinderServicePort = pathFinderServicePort;
		this.notificationCreatorProvider = notificationCreatorProvider;
        this.trackingStatusServicePort = trackingStatusServicePort;
        this.shipmentStatusHandlers = shipmentStatusHandlers;
        this.countryDetermineService = countryDetermineService;
        this.priceService = priceService;
        this.countryServiceAvailabilityService = countryServiceAvailabilityService;
        this.signatureService = signatureService;
    }

    @Override
    @Transactional
    public Result<ShipmentCreateResponse, ErrorCode> ship(final ShipmentCreateRequest request) {

        final CountryCode issuerCountryCode = request.getIssuerCountryCode();

        final CountryCode receiverCountryCode = request.getReceiverCountryCode();

        final boolean originCountryAvailable = this.countryServiceAvailabilityService.isCountryAvailable(issuerCountryCode);

        final boolean destinationCountryAvailable = this.countryServiceAvailabilityService.isCountryAvailable(receiverCountryCode);

        if (!originCountryAvailable) {
            return Result.failure(ErrorCode.ORIGIN_DEPARTMENT_NOT_AVAILABLE);
        } else if (!destinationCountryAvailable) {
            return Result.failure(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE);
        }

        final Address recipientAddress = Address.from(request.getSender());
        
        final Sender sender = request.getSender();
        
        final Recipient recipient = request.getRecipient();

		final Result<VoronoiResponse, ErrorCode> voronoiResponse = this.pathFinderServicePort
				.determineDeliveryDepartment(recipientAddress);
        
        if (voronoiResponse.isFailure()) {
            return Result.failure(voronoiResponse.getFailure());
        }

        final Price shipmentPrice = determineShipmentPrice(request);

        final ShipmentId shipmentId = this.shipmentService.nextShipmentId();

        final Country issuerCountry = this.countryDetermineService.determineCountryByCode(issuerCountryCode);

        final Country receiverCountry = this.countryDetermineService.determineCountryByCode(receiverCountryCode);

		final Shipment shipment = new Shipment(shipmentId, sender, recipient, request.getShipmentSize(), null,
                issuerCountry, receiverCountry, shipmentPrice.getMoney(), false,
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
    public Result<Void, ErrorCode> update(final ShipmentUpdateRequest request) {

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
                return Result.failure(ErrorCode.SHIPMENT_202);
            }
        }
        return Result.success();
    }

    @Override
    public Result<Void, ErrorCode> addDangerousGood(final DangerousGoodCreateRequest request) {
        final ShipmentId shipmentId = request.getShipmentId();
        if (!existsShipment(shipmentId)) {
            return Result.failure(ErrorCode.SHIPMENT_202);
        }

        this.shipmentService.changeDangerousGoodTo(shipmentId, DangerousGood.from(request));

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
    public void changePersonTo(final Person person, final ShipmentId shipmentId) {
        if (person.getType() == PersonType.SENDER) {
            this.shipmentService.changeSenderTo(shipmentId, (Sender) person);
        } else if (person.getType() == PersonType.RECIPIENT) {
            this.shipmentService.changeRecipientTo(shipmentId, (Recipient) person);
        }
    }

    @Override
    public void changeShipmentTypeTo(final ChangeShipmentTypeRequest request) {
        final Shipment shipment = this.shipmentService.find(request.shipmentId());

        if (shipment.getShipmentType() == request.shipmentType()) {
            throw new RestException(400, "Shipment type cannot be changed to the same type");
        }

        final Result<Void, String> validateShipment = this.shipmentStateValidatorService.validateShipmentState(shipment);

        if (validateShipment.isFailure()) {
            throw new RestException(400, validateShipment.getFailure());
        }

		if (request.shipmentType() == ShipmentType.CHILD) {
			final ShipmentId shipmentId = this.shipmentService.nextShipmentId();

			final Shipment newShipment = new Shipment(shipmentId, shipment.getSender(), shipment.getRecipient(),
					shipment.getShipmentSize(), shipment.getShipmentId(), shipment.getOriginCountry(),
					shipment.getDestinationCountry(), shipment.getPrice(), shipment.isLocked(),
					shipment.getDestination(), shipment.getSignature(), shipment.getShipmentPriority());
			this.shipmentService.changeShipmentTypeTo(request.shipmentId(), ShipmentType.PARENT, shipmentId);
			this.shipmentService.createShipment(newShipment);
		} else {
			this.shipmentService.changeShipmentTypeTo(request.shipmentId(), ShipmentType.PARENT, null);
			this.shipmentService.lockShipment(shipment.getShipmentRelatedId());
		}
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
    public void changeShipmentSignatureTo(final SignatureChangeRequest request, final SignatureMethod signatureMethod) {
        final Signature signature = Signature.from(request, signatureMethod);
        this.signatureService.createSignature(signature);
    }

    @Override
    public void changeIssuerCountryTo(final ShipmentCountryRequest request) {
        final ShipmentId shipmentId = request.shipmentId();
        final Country originCountry = this.countryDetermineService.determineCountryByCode(request.issuerCountry());
        this.shipmentService.changeShipmentIssuerCountryTo(shipmentId, originCountry);
    }

    @Override
    public void changeReceiverCountryTo(final ShipmentCountryRequest request) {
        final ShipmentId shipmentId = request.shipmentId();
        final Country destinationCountry = this.countryDetermineService.determineCountryByCode(request.receiverCountry());
        this.shipmentService.changeShipmentReceiverCountryTo(shipmentId, destinationCountry);
    }

    @Override
    public void changeShipmentCountries(final ShipmentCountryRequest request) {
        validateShipmentNotInStatus(request.shipmentId());
        final boolean issuerCountryAvailable = this.countryServiceAvailabilityService.isCountryAvailable(request.issuerCountry());
        final boolean receiverCountryAvailable = this.countryServiceAvailabilityService.isCountryAvailable(request.receiverCountry());

        if (issuerCountryAvailable) {
            final Country country = this.countryDetermineService.determineCountryByCode(request.issuerCountry());
            this.shipmentService.changeShipmentIssuerCountryTo(request.shipmentId(), country);
        }
        if (receiverCountryAvailable) {
            final Country country = this.countryDetermineService.determineCountryByCode(request.receiverCountry());
            this.shipmentService.changeShipmentReceiverCountryTo(request.shipmentId(), country);
        }
    }

    @Override
    public Shipment loadShipment(final ShipmentId shipmentId) {
        return this.shipmentService.find(shipmentId);
    }

    @Override
    public boolean existsShipment(final ShipmentId shipmentId) {
        return this.shipmentService.existsShipment(shipmentId);
    }

    private void logCreatedShipment(final Shipment shipment) {
        logger.info("Shipment {} has been created at {} with priority {}", shipment.getShipmentId().getValue(), shipment.getCreatedAt(),
                shipment.getShipmentPriority());
    }

    private void validateShipmentNotInStatus(final ShipmentId shipmentId) {
		final List<ShipmentStatus> shipmentStatuses = List.of(ShipmentStatus.REDIRECT, ShipmentStatus.REROUTE,
				ShipmentStatus.DELIVERY, ShipmentStatus.RETURN, ShipmentStatus.SENT);
        final Shipment shipment = loadShipment(shipmentId);
        if (shipmentStatuses.contains(shipment.getShipmentStatus())) {
            throw new RestException(400, "Cannot modify shipment issuer or receiver country");
        }
        if (shipment.getShipmentRelatedId() != null) {
            final Shipment relatedShipment = loadShipment(shipment.getShipmentRelatedId());
            if (shipmentStatuses.contains(relatedShipment.getShipmentStatus())) {
                throw new RestException(400, "Cannot modify parent shipment issuer or receiver country");
            }
        }
    }

}
