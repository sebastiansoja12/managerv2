package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.commonassets.model.Money;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.application.port.primary.command.ChangeShipmentTypeRequest;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentDeliveryCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentReturnCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentStatusRequest;
import com.warehouse.shipment.application.port.primary.command.ShipmentUpdateCommand;
import com.warehouse.shipment.application.port.primary.command.SignatureChangeRequest;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.secondary.Logger;
import com.warehouse.shipment.application.port.secondary.MailNotificationServicePort;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.application.port.secondary.ReturningServicePort;
import com.warehouse.shipment.application.port.secondary.ShipmentIdGenerator;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.application.port.secondary.ShipmentConfigurationPort;
import com.warehouse.shipment.application.service.CountryServiceAvailabilityService;
import com.warehouse.shipment.application.service.PriceService;
import com.warehouse.shipment.application.service.RouteLogService;
import com.warehouse.shipment.application.service.SignatureService;
import com.warehouse.shipment.application.service.TrackingNumberGenerationService;
import com.warehouse.shipment.domain.enumeration.PersonType;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.context.ShipmentEventContext;
import com.warehouse.shipment.domain.exception.ShipmentNotFoundException;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.service.*;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.domain.vo.conf.ShipmentMetrics;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;
import org.springframework.http.HttpStatusCode;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentRepository shipmentRepository;

    private final SpecificationRepository specificationShipmentRepository;

    private final ShipmentIdGenerator shipmentIdGenerator;

    private final Logger logger;

    private final PathFinderServicePort pathFinderServicePort;

    private final PriceService priceService;

    private final CountryServiceAvailabilityService countryServiceAvailabilityService;

    private final SignatureService signatureService;

    private final RouteLogService routeLogService;

    private final ReturningServicePort returningServicePort;

    private final MailNotificationServicePort mailNotificationServicePort;

    private final TrackingNumberGenerationService trackingNumberGenerationService;

    private final ShipmentConfigurationPort shipmentConfigurationServicePort;

    private final OperatorContextProvider operatorContextProvider;

    private final List<ShipmentStatus> shipmentStatuses = List.of(ShipmentStatus.REDIRECT,
            ShipmentStatus.DELIVERY, ShipmentStatus.RETURN, ShipmentStatus.SENT);

	public ShipmentPortImpl(final ShipmentRepository shipmentRepository,
                            final SpecificationRepository specificationShipmentRepository,
                            final ShipmentIdGenerator shipmentIdGenerator,
                            final Logger logger,
                            final PathFinderServicePort pathFinderServicePort,
                            final PriceService priceService,
                            final CountryServiceAvailabilityService countryServiceAvailabilityService,
                            final SignatureService signatureService,
                            final RouteLogService routeLogService,
                            final ReturningServicePort returningServicePort,
                            final MailNotificationServicePort mailNotificationServicePort,
                            final TrackingNumberGenerationService trackingNumberGenerationService,
                            final ShipmentConfigurationPort shipmentConfigurationServicePort,
                            final OperatorContextProvider operatorContextProvider) {
		this.shipmentRepository = shipmentRepository;
        this.specificationShipmentRepository = specificationShipmentRepository;
        this.shipmentIdGenerator = shipmentIdGenerator;
		this.logger = logger;
		this.pathFinderServicePort = pathFinderServicePort;
        this.priceService = priceService;
        this.countryServiceAvailabilityService = countryServiceAvailabilityService;
        this.signatureService = signatureService;
        this.routeLogService = routeLogService;
        this.returningServicePort = returningServicePort;
        this.mailNotificationServicePort = mailNotificationServicePort;
        this.trackingNumberGenerationService = trackingNumberGenerationService;
        this.shipmentConfigurationServicePort = shipmentConfigurationServicePort;
        this.operatorContextProvider = operatorContextProvider;
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
                .validateShipmentLimitations(shipmentConfiguration, ShipmentMetrics.from(command.getShipmentSize()));

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

        final ShipmentId shipmentId = this.shipmentIdGenerator.nextId();
        final TrackingNumber trackingNumber = this.trackingNumberGenerationService.generate(
                shipmentConfiguration.trackingNumberRule(), shipmentId);

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
                operatorContextProvider.currentDepartmentId().orElse(null),
                null,
                command.getShipmentPriority(),
                trackingNumber,
                workflowSettings.defaultStatus(),
                command.getDangerousGood()
        );

        this.shipmentRepository.createOrUpdate(shipment);
        logCreatedShipment(shipment);

        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentCreatedEvent(shipment.snapshot(), Instant.now()));

        return Result.success(new ShipmentCreateResponse(shipment.getExternalShipmentId(),
                shipment.getTrackingNumber().value()));
    }

    @Override
    @Transactional
    public Result<Void, ErrorCode> update(final ShipmentUpdateCommand command) {

        final Shipment shipment = this.find(command.getShipmentId());
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

        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentUpdated(shipment.snapshot(), Instant.now()));

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
        return this.findDangerousGood(shipmentId);
    }

    @Override
    public void putDangerousGood(final ShipmentId shipmentId, final DangerousGood dangerousGood) {
        this.changeDangerousGoodTo(shipmentId, dangerousGood);
    }

    @Override
    public void deleteDangerousGood(final ShipmentId shipmentId) {
        this.removeDangerousGood(shipmentId);
    }

    @Override
    public void processShipmentReturn(final ShipmentReturnCommand command) {
        final ReturnStatus returnStatus = command.getReturnStatus();
        final ShipmentId shipmentId = command.getShipmentId();
        switch (returnStatus) {
            case CREATED -> this.notifyShipmentReturned(shipmentId, command.getReason(),
                    command.getReasonCode(), command.getDepartmentCode());
            case COMPLETED -> this.lockShipment(shipmentId);
            case CANCELLED -> this.notifyReturnCanceled(shipmentId);
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

        final Shipment shipment = this.find(shipmentId);

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
            case DELIVERED -> this.notifyShipmentDelivered(shipmentId);
        }

        final Result<Void, ErrorCode> result =
                mailNotificationServicePort.notifyRecipient(deliveryStatus, this.loadShipment(shipmentId));

        if (result.isFailure()) {
            throw new RestException(400, result.getFailure().getMessage());
        }
    }

    @Override
    public void cancel(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.cancel(this.shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration().workflowSettings(),
                LocalDateTime.now());
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentCanceled(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeSenderTo(final ShipmentId shipmentId, final Sender sender) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeSender(sender);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentSenderChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeRecipientTo(final ShipmentId shipmentId, final Recipient recipient) {
        final Shipment shipment = this.find(shipmentId);
        if (!shipment.recipientCityMatches(recipient.getCity())) {
            final Result<VoronoiResponse, ErrorCode> voronoiResponse =
                    this.pathFinderServicePort.determineDeliveryDepartment(Address.from(recipient));
            if (voronoiResponse.isFailure()) {
                logger.warn("Cannot determine delivery department for recipient {}, skipping...", recipient);
            } else {
                this.changeDestination(shipmentId, voronoiResponse.getSuccess().getDepartmentCodeResult());
            }
        }
        shipment.changeRecipient(recipient);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentRecipientChanged(shipment.snapshot(), Instant.now()));
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
        final Shipment shipment = this.find(request.shipmentId());

        if (shipment.getShipmentType() == request.shipmentType()) {
            throw new RestException(400, "Shipment type cannot be changed to the same type");
        }

        final Result<Void, String> validateShipment = new ShipmentStateValidatorServiceImpl().validateShipmentState(shipment);

        if (validateShipment.isFailure()) {
            throw new RestException(400, validateShipment.getFailure());
        }

		if (request.shipmentType() == ShipmentType.CHILD) {
			final ShipmentId shipmentId = this.shipmentIdGenerator.nextId();
            final OperatorShipmentConfiguration shipmentConfiguration =
                    this.shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration();
            final TrackingNumber trackingNumber = this.trackingNumberGenerationService.generate(
                    shipmentConfiguration.trackingNumberRule(), shipmentId);
			final Shipment newShipment = Shipment.parentShipment(shipmentId, shipment.getSender(),
					shipment.getRecipient(), shipment.getShipmentSize(), shipment.getShipmentId(),
					shipment.getOriginCountry(), shipment.getDestinationCountry(), shipment.getPrice(),
					shipment.getDestination(), shipment.getOriginDepartmentId(), shipment.getSignature(), shipment.getShipmentPriority(), trackingNumber,
					shipmentConfiguration.workflowSettings().defaultStatus());
			this.changeShipmentTypeTo(request.shipmentId(), ShipmentType.CHILD, shipmentId);
			this.createShipment(newShipment);
        } else {
			this.changeShipmentTypeTo(request.shipmentId(), ShipmentType.PARENT, null);
			this.lockShipment(shipment.getShipmentRelatedId());
		}
    }

	@Override
	public void changeShipmentStatusTo(final ShipmentStatusRequest request) {
		final ShipmentStatus status = request.shipmentStatus();
		final ShipmentId shipmentId = request.shipmentId();
        switch (status) {
            case CREATED -> throw new IllegalStateException("Shipment already created, status cannot be changed");
            case REDIRECT -> notifyRelatedShipmentRedirected(shipmentId, this.shipmentIdGenerator.nextId());
            case REROUTE -> notifyShipmentRerouted(shipmentId);
            case SENT -> notifyShipmentSent(shipmentId);
            case DELIVERY -> notifyShipmentDelivered(shipmentId);
            case RETURN -> notifyShipmentReturned(shipmentId);
            default -> {
            }
        }
	}

    @Override
    public void changeShipmentSignatureTo(final SignatureChangeRequest request, final SignatureMethod signatureMethod) {
        final Signature signature = new Signature(
                request.getSignerName(),
                signatureMethod,
                request.getDocumentReference(),
                request.getShipmentId(),
                request.getSignature().getBytes(StandardCharsets.UTF_8));
        this.signatureService.createSignature(signature);
    }

    @Override
    public Shipment loadShipment(final ShipmentId shipmentId) {
        return this.find(shipmentId);
    }

    @Override
    public Shipment loadShipment(final TrackingNumber trackingNumber) {
        return this.find(trackingNumber);
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
        return this.search(criteria);
    }

    @Override
    public boolean existsShipment(final ShipmentId shipmentId) {
        return this.shipmentRepository.exists(shipmentId);
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

    @Override
    public void createShipment(final Shipment shipment) {
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentCreatedEvent(shipment.snapshot(), Instant.now()));
    }

    @Override
    public Shipment find(final ShipmentId shipmentId) {
        return this.shipmentRepository.findById(shipmentId);
    }

    @Override
    public Shipment find(final TrackingNumber trackingNumber) {
        return this.shipmentRepository.findByTrackingNumber(trackingNumber);
    }

    @Override
    public List<Shipment> search(final ShipmentSearchCriteria criteria) {
        return this.specificationShipmentRepository.list(criteria);
    }

    @Override
    public void changeShipmentTypeTo(final ShipmentId shipmentId,
                                     final ShipmentType shipmentType,
                                     final ShipmentId relatedShipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        if (relatedShipmentId == null) {
            shipment.changeShipmentType(shipmentType);
        } else {
            shipment.changeShipmentTypeWithRelatedId(shipmentType, relatedShipmentId);
        }
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentTypeChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeShipmentStatusTo(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentStatus(shipmentStatus);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentStatusChangedEvent(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeShipmentRelatedIdTo(final ShipmentId shipmentId, final ShipmentId relatedShipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentRelatedId(relatedShipmentId);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentUpdated(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeShipmentPriorityTo(final ShipmentId shipmentId, final ShipmentPriority shipmentPriority) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeShipmentPriority(shipmentPriority);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentUpdated(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeCurrencyTo(final ShipmentId shipmentId, final Currency currency) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeCurrency(currency);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentCurrencyChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeShipmentIssuerCountryTo(final ShipmentId shipmentId, final CountryCode originCountry) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeIssuerCountry(originCountry);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentCountriesChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeShipmentReceiverCountryTo(final ShipmentId shipmentId, final CountryCode destinationCountry) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeReceiverCountry(destinationCountry);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentCountriesChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeSignatureRequiredTo(final ShipmentId shipmentId, final boolean signatureRequired) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeSignatureRequired(signatureRequired);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentUpdated(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeDangerousGoodTo(final ShipmentId shipmentId, final DangerousGood dangerousGood) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeDangerousGood(dangerousGood);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentDangerousGoodUpdated(shipment.snapshot(), Instant.now()));
    }

    @Override
    public Optional<DangerousGood> findDangerousGood(final ShipmentId shipmentId) {
        return Optional.ofNullable(this.shipmentRepository.findById(shipmentId).getDangerousGood());
    }

    @Override
    public void removeDangerousGood(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.removeDangerousGood();
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentDangerousGoodRemoved(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyRelatedShipmentRedirected(final ShipmentId shipmentId, final ShipmentId relatedShipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyRelatedShipmentRedirected(relatedShipmentId);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentRedirected(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyShipmentRerouted(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentRerouted();
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentRerouted(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyRelatedShipmentLocked(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyRelatedShipmentLocked();
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentRelatedLocked(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyShipmentSent(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentSent();
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentSent(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyShipmentReturned(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentReturned();
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentReturned(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyShipmentReturned(final ShipmentId shipmentId,
                                       final String reason,
                                       final ReasonCode reasonCode,
                                       final DepartmentCode departmentCode) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentReturned();
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentReturnCreated(
                shipment.snapshot(), reasonCode, reason, departmentCode, Instant.now()));
    }

    @Override
    public void notifyShipmentDelivered(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentDelivered();
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentDelivered(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void notifyReturnCanceled(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentReturnCanceled();
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentReturnCanceled(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changeShipmentCountries(final ShipmentCountryRequest request) {
        final Shipment shipment = this.shipmentRepository.findById(request.shipmentId());
        shipment.updateCountries(request);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentCountriesChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void lockShipment(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.lockShipment();
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentLocked(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void redirectShipmentToSender(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        final ShipmentId redirectedShipmentId = shipment.getShipmentRelatedId();
        final TrackingNumber trackingNumber = this.trackingNumberGenerationService.generate(
                this.shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration().trackingNumberRule(),
                redirectedShipmentId);
        final Shipment redirectedShipment = shipment.redirectToSender(redirectedShipmentId, trackingNumber);
        this.shipmentRepository.createOrUpdate(redirectedShipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentUpdated(redirectedShipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void changeDestination(final ShipmentId shipmentId, final DepartmentCode destination) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeDestinationDepartment(destination);
        this.shipmentRepository.createOrUpdate(shipment);
        ShipmentEventContext.eventPublisher().publishEvent(new ShipmentDestinationChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public Shipment findByExternalId(final ExternalId<String> externalId) {
        return this.shipmentRepository.findByExternalId(externalId).orElseThrow(
                () -> new ShipmentNotFoundException(HttpStatusCode.valueOf(404).value(), "Shipment not found"));
    }

}
