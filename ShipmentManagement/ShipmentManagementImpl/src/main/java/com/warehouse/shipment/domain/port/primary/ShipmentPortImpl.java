package com.warehouse.shipment.domain.port.primary;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
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
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.service.*;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.domain.vo.conf.ShipmentLabelSettings;
import com.warehouse.shipment.domain.vo.conf.ShipmentMetrics;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;


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

    private final RouteLogService routeLogService;

    private final ReturningServicePort returningServicePort;

    private final MailNotificationServicePort mailNotificationServicePort;

    private final TrackingNumberService trackingNumberService;

    private final ShipmentConfigurationServicePort shipmentConfigurationServicePort;

    private final List<ShipmentStatus> shipmentStatuses = List.of(ShipmentStatus.REDIRECT,
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
                            final RouteLogService routeLogService,
                            final ReturningServicePort returningServicePort,
                            final MailNotificationServicePort mailNotificationServicePort,
                            final TrackingNumberService trackingNumberService,
                            final ShipmentConfigurationServicePort shipmentConfigurationServicePort) {
		this.shipmentService = shipmentService;
		this.logger = logger;
		this.pathFinderServicePort = pathFinderServicePort;
		this.notificationCreatorProvider = notificationCreatorProvider;
        this.shipmentStatusHandlers = shipmentStatusHandlers;
        this.countryDetermineService = countryDetermineService;
        this.priceService = priceService;
        this.countryServiceAvailabilityService = countryServiceAvailabilityService;
        this.signatureService = signatureService;
        this.routeLogService = routeLogService;
        this.returningServicePort = returningServicePort;
        this.mailNotificationServicePort = mailNotificationServicePort;
        this.trackingNumberService = trackingNumberService;
        this.shipmentConfigurationServicePort = shipmentConfigurationServicePort;
    }

    @Override
    @Transactional
    public Result<ShipmentCreateResponse, ErrorCode> ship(final ShipmentCreateCommand command) {

        final OperatorShipmentConfiguration shipmentConfiguration =
                this.shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration();

        final CountryCode issuerCountryCode = command.getIssuerCountryCode();
        final CountryCode receiverCountryCode = command.getReceiverCountryCode();

        final Result<Void, ErrorCode> countryValidation =
                validateCountries(issuerCountryCode, receiverCountryCode);

        if (countryValidation.isFailure()) {
            return Result.failure(countryValidation.getFailure());
        }

        final Result<Void, String> shipmentLimitationValidationResult = new ShipmentStateValidatorServiceImpl()
                .validateShipmentLimitations(shipmentConfiguration, ShipmentMetrics.from(command));

        if (shipmentLimitationValidationResult.isFailure()) {
            return Result.failure(ErrorCode.SHIPMENT_EXTENDED_LIMITATIONS);
        }

        final Sender sender = command.getSender();
        final Recipient recipient = command.getRecipient();
        final Address recipientAddress = Address.from(recipient);

        final Result<VoronoiResponse, ErrorCode> voronoiResponse =
                this.pathFinderServicePort.determineDeliveryDepartment(recipientAddress);

        if (voronoiResponse.isFailure()) {
            return Result.failure(voronoiResponse.getFailure());
        }

        final Price shipmentPrice =
                resolveShipmentPrice(command.getPrice(), command.getShipmentSize());

        final ShipmentWorkflowSettings workflowSettings = shipmentConfiguration.workflowSettings();

        final ShipmentId shipmentId = this.shipmentService.nextShipmentId();
        final TrackingNumber trackingNumber =
                this.trackingNumberService.nextTrackingNumber(shipmentConfiguration.trackingNumberRule(), shipmentId);

        final Shipment shipment = new Shipment(
                shipmentId,
                sender,
                recipient,
                command.getShipmentSize(),
                null,
                issuerCountryCode,
                receiverCountryCode,
                shipmentPrice.getMoney(),
                false,
                voronoiResponse.getSuccess().getDepartmentCodeResult(),
                null,
                command.getShipmentPriority(),
                trackingNumber,
                workflowSettings.defaultStatus()
        );
        shipment.setDangerousGood(command.getDangerousGood());

        this.shipmentService.createShipment(shipment);
        logCreatedShipment(shipment);

        final ShipmentLabelSettings shipmentLabelSettings = shipmentConfiguration.labelSettings();

        return Result.success(new ShipmentCreateResponse(shipment.getExternalShipmentId(),
                shipment.getTrackingNumber().value()));
    }

    @Override
    @Transactional
    public Result<Void, ErrorCode> update(final ShipmentUpdateCommand command) {

        final Shipment shipment = this.shipmentService.find(command.getShipmentId());
        if (shipment == null) {
            return Result.failure(ErrorCode.SHIPMENT_204);
        }

        final ShipmentConfiguration configuration = command.getShipmentConfiguration();
        if (shouldValidateState(configuration)) {
            final Result<Void, String> validation =
                    new ShipmentStateValidatorServiceImpl().validateShipmentState(shipment);
            if (validation.isFailure()) {
                return Result.failure(ErrorCode.SHIPMENT_203);
            }
        }

        final CountryCode issuerCountryCode = command.getIssuerCountryCode();
        final CountryCode receiverCountryCode = command.getReceiverCountryCode();

        final Result<Void, ErrorCode> countryValidation =
                validateCountries(issuerCountryCode, receiverCountryCode);
        if (countryValidation.isFailure()) {
            return Result.failure(countryValidation.getFailure());
        }

        final DepartmentCode destination = resolveDestination(command, shipment, configuration);

        final Price shipmentPrice =
                resolveShipmentPrice(command.getPrice(), command.getShipmentSize());

        shipment.update(
                command.getSender(),
                command.getRecipient(),
                command.getShipmentStatus(),
                command.getShipmentPriority(),
                command.getShipmentSize(),
                shipmentPrice.getMoney(),
                command.getDangerousGood(),
                destination,
                false
        );

        this.shipmentService.update(shipment);
        publishIfNeeded(shipment.snapshot(), configuration);

        return Result.success();
    }

    private Result<Void, ErrorCode> validateCountries(
            final CountryCode issuerCountryCode, final CountryCode receiverCountryCode) {

        if (!this.countryServiceAvailabilityService.isCountryAvailable(issuerCountryCode)) {
            return Result.failure(ErrorCode.ORIGIN_DEPARTMENT_NOT_AVAILABLE);
        }

        if (!this.countryServiceAvailabilityService.isCountryAvailable(receiverCountryCode)) {
            return Result.failure(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE);
        }

        return Result.success();
    }

    private Price resolveShipmentPrice(final Money price, final ShipmentSize shipmentSize) {
        return price == null || !price.isDefined()
                ? this.priceService.determineShipmentPrice(shipmentSize, Currency.PLN)
                : new Price(price);
    }

    @Override
    public Optional<DangerousGood> loadDangerousGood(final ShipmentId shipmentId) {
        return this.shipmentService.findDangerousGood(shipmentId);
    }

    @Override
    public void putDangerousGood(final ShipmentId shipmentId, final DangerousGood dangerousGood) {
        this.shipmentService.changeDangerousGoodTo(shipmentId, dangerousGood);
    }

    @Override
    public void deleteDangerousGood(final ShipmentId shipmentId) {
        this.shipmentService.removeDangerousGood(shipmentId);
    }

    @Override
    public void processShipmentReturn(final ShipmentReturnCommand command) {
        final ReturnStatus returnStatus = command.getReturnStatus();
        final ShipmentId shipmentId = command.getShipmentId();
        switch (returnStatus) {
            case CREATED -> this.shipmentService.notifyShipmentReturned(shipmentId, command.getReason(),
                    command.getReasonCode(), command.getDepartmentCode());
            case COMPLETED -> this.shipmentService.lockShipment(shipmentId);
            case CANCELLED -> this.shipmentService.notifyReturnCanceled(shipmentId);
        }
    }

    @Override
    public void cancelShipmentReturn(final ReturnId returnId) {
        final ShipmentReturnDetails returnDetails = this.returningServicePort.getReturn(returnId);
        this.processShipmentReturn(new ShipmentReturnCommand(
                returnDetails.assignedDepartmentCode(),
                returnDetails.reason(),
                returnDetails.shipmentId(),
                ReturnStatus.CANCELLED,
                returnDetails.reasonCode()));
    }

    @Override
    public ShipmentReturnDetails loadShipmentReturn(final ReturnId returnId) {
        return this.returningServicePort.getReturn(returnId);
    }

    @Override
    public ShipmentReturnPage loadShipmentReturns(
            final DepartmentCode departmentCode, final int page, final int size) {
        return this.returningServicePort.getReturns(departmentCode, page, size);
    }

    @Override
    @Transactional
    public void processShipmentDelivery(final ShipmentDeliveryCommand command) {
        final DeliveryStatus deliveryStatus = command.getDeliveryStatus();
        final ShipmentId shipmentId = command.getShipmentId();

        final Shipment shipment = this.shipmentService.find(shipmentId);

        if (shipment.isFullyDelivered()) {
            throw new RestException(400, "Shipment is fully delivered");
        }

        switch (deliveryStatus) {
            case DELIVERY, DEPOT -> changeShipmentStatusTo(new ShipmentStatusRequest(shipmentId, ShipmentStatus.DELIVERY));
            case RETURN -> changeShipmentStatusTo(new ShipmentStatusRequest(shipmentId, ShipmentStatus.RETURN));
            case UNAVAILABLE, REJECTED, SENDER -> {
                changeShipmentStatusTo(new ShipmentStatusRequest(shipmentId, ShipmentStatus.REDIRECT));
            }
            case UNKNOWN, LOST -> changeShipmentStatusTo(new ShipmentStatusRequest(shipmentId, ShipmentStatus.SENT));
            case DELIVERED -> this.shipmentService.notifyShipmentDelivered(shipmentId);
        }

        final Result<Void, ErrorCode> result =
                mailNotificationServicePort.notifyRecipient(deliveryStatus, this.loadShipment(shipmentId));

        if (result.isFailure()) {
            throw new RestException(400, result.getFailure().getMessage());
        }
    }

    @Override
    public void changeSenderTo(final ShipmentId shipmentId, final Sender sender) {
        this.shipmentService.changeSenderTo(shipmentId, sender);
    }

    @Override
    public void changeRecipientTo(final ShipmentId shipmentId, final Recipient recipient) {
        final Shipment shipment = this.shipmentService.find(shipmentId);
        if (!shipment.recipientCityMatches(recipient.getCity())) {
            final Result<VoronoiResponse, ErrorCode> voronoiResponse =
                    this.pathFinderServicePort.determineDeliveryDepartment(Address.from(recipient));
            if (voronoiResponse.isFailure()) {
                logger.warn("Cannot determine delivery department for recipient {}, skipping...", recipient);
            } else {
                this.shipmentService.changeDestination(shipmentId, voronoiResponse.getSuccess().getDepartmentCodeResult());
            }
        }
        this.shipmentService.changeRecipientTo(shipmentId, recipient);
    }

    @Override
    public void changePersonTo(final Person person, final ShipmentId shipmentId) {
        validateShipmentNotInStatus(shipmentId);
        if (person.getType() == PersonType.SENDER) {
            changeSenderTo(shipmentId, (Sender) person);
        } else if (person.getType() == PersonType.RECIPIENT) {
            changeRecipientTo(shipmentId, (Recipient) person);
        }
    }

    @Override
    public void changeShipmentTypeTo(final ChangeShipmentTypeRequest request) {
        final Shipment shipment = this.shipmentService.find(request.shipmentId());

        if (shipment.getShipmentType() == request.shipmentType()) {
            throw new RestException(400, "Shipment type cannot be changed to the same type");
        }

        final Result<Void, String> validateShipment = new ShipmentStateValidatorServiceImpl().validateShipmentState(shipment);

        if (validateShipment.isFailure()) {
            throw new RestException(400, validateShipment.getFailure());
        }

		if (request.shipmentType() == ShipmentType.CHILD) {
			final ShipmentId shipmentId = this.shipmentService.nextShipmentId();
            final OperatorShipmentConfiguration shipmentConfiguration =
                    this.shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration();
            final TrackingNumber trackingNumber =
                    this.trackingNumberService.nextTrackingNumber(shipmentConfiguration.trackingNumberRule(), shipmentId);
			final Shipment newShipment = Shipment.parentShipment(shipmentId, shipment.getSender(),
					shipment.getRecipient(), shipment.getShipmentSize(), shipment.getShipmentId(),
					shipment.getOriginCountry(), shipment.getDestinationCountry(), shipment.getPrice(),
					shipment.getDestination(), shipment.getSignature(), shipment.getShipmentPriority(), trackingNumber,
					shipmentConfiguration.workflowSettings().defaultStatus());
			this.shipmentService.changeShipmentTypeTo(request.shipmentId(), ShipmentType.CHILD, shipmentId);
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
    public Shipment loadShipment(final ShipmentId shipmentId) {
        return this.shipmentService.find(shipmentId);
    }

    @Override
    public Shipment loadShipment(final TrackingNumber trackingNumber) {
        return this.shipmentService.find(trackingNumber);
    }

    @Override
    public ShipmentRouteLog getShipmentByShipmentId(final ShipmentId shipmentId) {
        return listShipmentsWithTracking(loadShipment(shipmentId));
    }

    @Override
    public ShipmentRouteLog getShipmenyByTrackingNumber(final TrackingNumber trackingNumber) {
        return listShipmentsWithTracking(loadShipment(trackingNumber));
    }

    private ShipmentRouteLog listShipmentsWithTracking(final Shipment shipment) {
        return new ShipmentRouteLog(shipment,
                this.routeLogService.findByShipmentId(shipment.getShipmentId()).orElse(null));
    }

    @Override
    public List<Shipment> searchShipments(final ShipmentSearchCriteria criteria) {
        return this.shipmentService.search(criteria);
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
            throw new RestException(400, "Cannot modify shipment in current status");
        }
        if (shipment.getShipmentRelatedId() != null) {
            final Shipment relatedShipment = loadShipment(shipment.getShipmentRelatedId());
            if (shipmentStatuses.contains(relatedShipment.getShipmentStatus())) {
                throw new RestException(400, "Cannot modify child shipment");
            }
        }
    }

    private boolean shouldValidateState(final ShipmentConfiguration configuration) {
        return !configuration.forceUpdate();
    }

    private DepartmentCode resolveDestination(final ShipmentUpdateCommand command,
                                      final Shipment shipment,
                                      final ShipmentConfiguration configuration) {

        if (configuration.customRerouteDepartment()) {
            return command.getDestination();
        }

        final Address address = Address.from(command.getShipmentStatus()
                .equals(ShipmentStatus.RETURN) ? command.getSender() : command.getRecipient());

        final Result<VoronoiResponse, ErrorCode> voronoiResult =
                this.pathFinderServicePort.determineDeliveryDepartment(address);

        return voronoiResult.isSuccess()
                ? voronoiResult.getSuccess().getDepartmentCodeResult()
                : shipment.getDestination();
    }

    private void publishIfNeeded(final ShipmentSnapshot snapshot, final ShipmentConfiguration configuration) {
		if (configuration.publishInReturnManager()) {
			this.returningServicePort.notifyShipmentUpdated(snapshot);
		}
    }
}
