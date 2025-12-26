package com.warehouse.shipment.domain.port.primary;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.enumeration.PersonType;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.handler.ShipmentDefaultHandler;
import com.warehouse.shipment.domain.handler.ShipmentStatusHandler;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.service.*;
import com.warehouse.shipment.domain.vo.*;


public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentService shipmentService;

    private final Logger logger;

    private final PathFinderServicePort pathFinderServicePort;

    private final NotificationCreatorProvider notificationCreatorProvider;

    private final Set<ShipmentStatusHandler> shipmentStatusHandlers;

    private final CountryDetermineService countryDetermineService;

    private final PriceService priceService;

    private final CountryServiceAvailabilityService countryServiceAvailabilityService;

    private final SignatureService signatureService;

    private final ShipmentStateValidatorService shipmentStateValidatorService = new ShipmentStateValidatorServiceImpl();

    private final RouteLogServicePort routeLogServicePort;

    private final ReturningServicePort returningServicePort;

    private final List<ShipmentStatus> shipmentStatuses = List.of(ShipmentStatus.REDIRECT, ShipmentStatus.REROUTE,
            ShipmentStatus.DELIVERY, ShipmentStatus.RETURN, ShipmentStatus.SENT);

	public ShipmentPortImpl(final ShipmentService shipmentService,
                            final Logger logger,
                            final PathFinderServicePort pathFinderServicePort,
                            final NotificationCreatorProvider notificationCreatorProvider,
                            final Set<ShipmentStatusHandler> shipmentStatusHandlers,
                            final CountryDetermineService countryDetermineService,
                            final PriceService priceService,
                            final CountryServiceAvailabilityService countryServiceAvailabilityService,
                            final SignatureService signatureService,
                            final RouteLogServicePort routeLogServicePort,
                            final ReturningServicePort returningServicePort) {
		this.shipmentService = shipmentService;
		this.logger = logger;
		this.pathFinderServicePort = pathFinderServicePort;
		this.notificationCreatorProvider = notificationCreatorProvider;
        this.shipmentStatusHandlers = shipmentStatusHandlers;
        this.countryDetermineService = countryDetermineService;
        this.priceService = priceService;
        this.countryServiceAvailabilityService = countryServiceAvailabilityService;
        this.signatureService = signatureService;
        this.routeLogServicePort = routeLogServicePort;
        this.returningServicePort = returningServicePort;
    }

    @Override
    @Transactional
    public Result<ShipmentCreateResponse, ErrorCode> ship(final ShipmentCreateCommand request) {

        final CountryCode issuerCountryCode = request.getIssuerCountryCode();

        final CountryCode receiverCountryCode = request.getReceiverCountryCode();

        final boolean originCountryAvailable = this.countryServiceAvailabilityService.isCountryAvailable(issuerCountryCode);

        final boolean destinationCountryAvailable = this.countryServiceAvailabilityService.isCountryAvailable(receiverCountryCode);

        if (!originCountryAvailable) {
            return Result.failure(ErrorCode.ORIGIN_DEPARTMENT_NOT_AVAILABLE);
        } else if (!destinationCountryAvailable) {
            return Result.failure(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE);
        }

        final Sender sender = request.getSender();
        
        final Recipient recipient = request.getRecipient();

        final Address recipientAddress = Address.from(recipient);

        final Result<VoronoiResponse, ErrorCode> voronoiResponse = this.pathFinderServicePort
				.determineDeliveryDepartment(recipientAddress);
        
        if (voronoiResponse.isFailure()) {
            return Result.failure(voronoiResponse.getFailure());
        }

        final Price shipmentPrice = determineShipmentPrice(request);

        final ShipmentId shipmentId = this.shipmentService.nextShipmentId();

		final Shipment shipment = new Shipment(shipmentId, sender, recipient, request.getShipmentSize(), null,
                issuerCountryCode, receiverCountryCode, shipmentPrice.getMoney(), false,
				voronoiResponse.getSuccess().getValue(), null, request.getShipmentPriority());

        this.shipmentService.createShipment(shipment);

        logCreatedShipment(shipment);

        return Result.success(new ShipmentCreateResponse(shipmentId));
    }

    private Price determineShipmentPrice(final ShipmentCreateCommand request) {
        return request.getPrice() == null || !request.getPrice().isDefined() ?
                this.priceService.determineShipmentPrice(request.getShipmentSize(), Currency.PLN) : new Price(request.getPrice());
    }

    @Override
    @Transactional
    public Result<Void, ErrorCode> update(final ShipmentUpdateCommand command) {

        final ShipmentId shipmentId = command.getShipmentId();

        final Shipment shipment = this.shipmentService.find(shipmentId);

        if (shipment == null) {
            return Result.failure(ErrorCode.SHIPMENT_204);
        }

        final ShipmentConfiguration configuration = command.getShipmentConfiguration();

        if (configuration != null && !configuration.forceUpdate()) {
            final Result<Void, String> resultValidation = shipmentStateValidatorService.validateShipmentState(shipment);
        }

        final String destination;
        if (configuration != null && configuration.customRerouteDepartment()) {
            destination = command.getDestination();
        } else {
            final Result<VoronoiResponse, ErrorCode> voronoiResult =
                    this.pathFinderServicePort.determineDeliveryDepartment(Address.from(shipment.getRecipient()));
            destination = voronoiResult.isSuccess() ? voronoiResult.getSuccess().getValue() : "NCS";
        }

        final Sender sender = command.getSender();
        final Recipient recipient = command.getRecipient();
        final ShipmentStatus shipmentStatus = command.getShipmentStatus();
        final ShipmentPriority shipmentPriority = command.getShipmentPriority();
        final Money price = command.getPrice();
        final ShipmentSize shipmentSize = command.getShipmentSize();
        final DangerousGood dangerousGood = command.getDangerousGood();
        
		shipment.update(sender, recipient, shipmentStatus, shipmentPriority, shipmentSize, price, dangerousGood,
                destination, false);

        this.shipmentService.update(shipment);

        if (configuration != null && configuration.publishInRouteTracker()) {
            routeLogServicePort.notifyShipmentUpdated(shipment.snapshot());
        }

        if (configuration != null && configuration.publishInReturnManager()) {
            returningServicePort.notifyShipmentUpdated(shipment.snapshot());
        }

        return Result.success();
    }

    @Override
    public Result<Void, ErrorCode> addDangerousGood(final DangerousGoodCreateCommand command) {
        final ShipmentId shipmentId = command.getShipmentId();
        if (!existsShipment(shipmentId)) {
            return Result.failure(ErrorCode.SHIPMENT_202);
        }

        this.shipmentService.changeDangerousGoodTo(shipmentId, DangerousGood.from(command));

        return Result.success();
    }

    @Override
    public void processShipmentReturn(final ShipmentReturnCommand command) {
        final ReturnStatus returnStatus = command.getReturnStatus();
        final ShipmentId shipmentId = command.getShipmentId();
        switch (returnStatus) {
            case CREATED ->  this.shipmentService.notifyShipmentReturned(shipmentId);
            case COMPLETED -> this.shipmentService.lockShipment(shipmentId);
            case CANCELLED -> this.shipmentService.notifyReturnCanceled(shipmentId);
        }
    }

    @Override
    public void changeSenderTo(final ShipmentId shipmentId, final Sender sender) {
        validateShipmentNotInStatus(shipmentId);
        this.shipmentService.changeSenderTo(shipmentId, sender);
    }

    @Override
    public void changeRecipientTo(final ShipmentId shipmentId, final Recipient recipient) {
        validateShipmentNotInStatus(shipmentId);
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
		final ShipmentStatus status = request.shipmentStatus();
		final ShipmentId shipmentId = request.shipmentId();
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
        this.shipmentService.changeShipmentIssuerCountryTo(shipmentId, request.issuerCountry());
    }

    @Override
    public void changeReceiverCountryTo(final ShipmentCountryRequest request) {
        final ShipmentId shipmentId = request.shipmentId();
        this.shipmentService.changeShipmentReceiverCountryTo(shipmentId, request.receiverCountry());
    }

    @Override
    public void changeShipmentCountries(final ShipmentCountryRequest request) {
        validateShipmentNotInStatus(request.shipmentId());
        final boolean issuerCountryAvailable = this.countryServiceAvailabilityService.isCountryAvailable(request.issuerCountry());
        final boolean receiverCountryAvailable = this.countryServiceAvailabilityService.isCountryAvailable(request.receiverCountry());

        if (issuerCountryAvailable) {
            final CountryCode country = request.issuerCountry();
            this.shipmentService.changeShipmentIssuerCountryTo(request.shipmentId(), country);
        }
        if (receiverCountryAvailable) {
            final CountryCode country = request.receiverCountry();
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
        final Shipment shipment = loadShipment(shipmentId);
        if (shipment == null) {
            throw new RestException(404, "Shipment not found");
        }
        if (shipmentStatuses.contains(shipment.getShipmentStatus())) {
            throw new RestException(400, "Cannot modify shipment issuer or receiver country");
        }
        if (shipment.getShipmentRelatedId() != null) {
            final Shipment relatedShipment = loadShipment(shipment.getShipmentRelatedId());
            if (shipmentStatuses.contains(relatedShipment.getShipmentStatus())) {
                throw new RestException(400, "Cannot modify parent shipment");
            }
        }
    }

}
