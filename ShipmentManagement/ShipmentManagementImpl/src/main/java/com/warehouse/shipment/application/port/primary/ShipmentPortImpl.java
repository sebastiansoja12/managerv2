package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.application.port.primary.command.*;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.application.port.secondary.*;
import com.warehouse.shipment.application.service.DepartmentCountryAvailabilityService;
import com.warehouse.shipment.application.service.ShipmentDepartmentResolutionService;
import com.warehouse.shipment.application.service.ShipmentResultFactory;
import com.warehouse.shipment.application.service.SignatureService;
import com.warehouse.shipment.application.service.TrackingNumberGenerationService;
import com.warehouse.shipment.application.service.delivery.ShipmentDeliveryStrategyResolver;
import com.warehouse.shipment.application.service.status.ShipmentStatusChangeStrategyResolver;
import com.warehouse.shipment.domain.enumeration.PersonType;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.service.ShipmentStateValidatorServiceImpl;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.domain.vo.conf.ShipmentMetrics;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;


public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentRepository shipmentRepository;

    private final Logger logger;

    private final PathFinderServicePort pathFinderServicePort;

    private final DepartmentCountryAvailabilityService departmentCountryAvailabilityService;

    private final SignatureService signatureService;

    private final ShipmentResultFactory shipmentResultFactory;

    private final MailNotificationServicePort mailNotificationServicePort;

    private final TrackingNumberGenerationService trackingNumberGenerationService;

    private final ShipmentConfigurationPort shipmentConfigurationServicePort;

    private final ShipmentDeliveryStrategyResolver shipmentDeliveryStrategyResolver;

    private final ShipmentStatusChangeStrategyResolver shipmentStatusChangeStrategyResolver;

    private final DomainEventPublisher domainEventPublisher;

    private final DepartmentServicePort departmentServicePort;

    private final ShipmentDepartmentResolutionService shipmentDepartmentResolutionService;

	public ShipmentPortImpl(final ShipmentRepository shipmentRepository,
                            final Logger logger,
                            final PathFinderServicePort pathFinderServicePort,
                            final DepartmentCountryAvailabilityService departmentCountryAvailabilityService,
                            final SignatureService signatureService,
                            final ShipmentResultFactory shipmentResultFactory,
                            final MailNotificationServicePort mailNotificationServicePort,
                            final TrackingNumberGenerationService trackingNumberGenerationService,
                            final ShipmentConfigurationPort shipmentConfigurationServicePort,
                            final ShipmentDeliveryStrategyResolver shipmentDeliveryStrategyResolver,
                            final ShipmentStatusChangeStrategyResolver shipmentStatusChangeStrategyResolver,
                            final DomainEventPublisher domainEventPublisher,
                            final DepartmentServicePort departmentServicePort,
                            final ShipmentDepartmentResolutionService shipmentDepartmentResolutionService) {
		this.shipmentRepository = shipmentRepository;
		this.logger = logger;
		this.pathFinderServicePort = pathFinderServicePort;
        this.departmentCountryAvailabilityService = departmentCountryAvailabilityService;
        this.signatureService = signatureService;
        this.shipmentResultFactory = shipmentResultFactory;
        this.mailNotificationServicePort = mailNotificationServicePort;
        this.trackingNumberGenerationService = trackingNumberGenerationService;
        this.shipmentConfigurationServicePort = shipmentConfigurationServicePort;
        this.shipmentDeliveryStrategyResolver = shipmentDeliveryStrategyResolver;
        this.shipmentStatusChangeStrategyResolver = shipmentStatusChangeStrategyResolver;
        this.domainEventPublisher = domainEventPublisher;
        this.departmentServicePort = departmentServicePort;
        this.shipmentDepartmentResolutionService = shipmentDepartmentResolutionService;
    }

    @Override
    @Transactional
    public Result<ShipmentCreateResponse, ErrorCode> ship(final ShipmentCreateCommand command) {

        final OperatorShipmentConfiguration shipmentConfiguration =
                this.shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration();

        final Party sender = command.getSender();
        final Party recipient = command.getRecipient();

        final Result<Void, ErrorCode> countryValidation =
                validateCountries(sender.getCountryCode(), recipient.getCountryCode());

        if (countryValidation.isFailure()) {
            return Result.failure(countryValidation.getFailure());
        }

        final ShipmentMetrics shipmentMetrics = ShipmentMetrics.from(
                command.getDimensions(), command.getWeight(), command.getDeclaredValue());
        final Result<Void, String> shipmentLimitationValidationResult = new ShipmentStateValidatorServiceImpl()
                .validateShipmentLimitations(shipmentConfiguration, shipmentMetrics);

        if (shipmentLimitationValidationResult.isFailure()) {
            return Result.failure(ErrorCode.SHIPMENT_EXTENDED_LIMITATIONS);
        }

        final PickupMethod pickupMethod = command.getPickupMethod();
        final ShipmentWorkflowSettings workflowSettings = shipmentConfiguration.workflowSettings();
        final ShipmentStatus initialStatus = pickupMethod.isPickupPointBased()
                ? ShipmentStatus.PLANNED
                : workflowSettings.defaultStatus();

        final Result<DepartmentId, ErrorCode> targetDepartment =
                shipmentDepartmentResolutionService.resolveTargetDepartmentId(command, recipient);
        if (targetDepartment.isFailure()) {
            return Result.failure(targetDepartment.getFailure());
        }

        final Result<DepartmentId, ErrorCode> originDepartment =
                shipmentDepartmentResolutionService.resolveOriginDepartmentId(command);
        if (originDepartment.isFailure()) {
            return Result.failure(originDepartment.getFailure());
        }

        final ShipmentId shipmentId = ShipmentId.nextId();
        final TrackingNumber trackingNumber = this.trackingNumberGenerationService.generate(workflowSettings,
                shipmentConfiguration.trackingNumberRule(), shipmentId);

        final Shipment shipment = new Shipment(
                shipmentId,
                sender,
                recipient,
                null,
                command.getPrice(),
                false,
                targetDepartment.getSuccess(),
                originDepartment.getSuccess(),
                command.getShipmentPriority(),
                trackingNumber,
                initialStatus,
                pickupMethod,
                command.getDeliveryMethod(),
                pickupMethod.isPickupPointBased() ? command.getPickupPointId() : null,
                command.getDeliveryMethod().isPickupPointBased() ? command.getDeliveryPickupPointId() : null,
                command.getDimensions(),
                command.getWeight(),
                command.getCustomerReference(),
                command.getContentDescription(),
                command.getDeclaredValue(),
                command.getServiceLevel() == null ? workflowSettings.defaultServiceLevel() : command.getServiceLevel(),
                command.getPackagingType()
        );

        this.shipmentRepository.createOrUpdate(shipment);
        logCreatedShipment(shipment);

        this.domainEventPublisher.publish(new ShipmentCreated(shipment.snapshot(), Instant.now()));

        return Result.success(new ShipmentCreateResponse(shipment.getExternalShipmentId(),
                shipment.getTrackingNumber().value()));
    }

    private Result<Void, ErrorCode> validateCountries(
            final CountryCode senderCountryCode, final CountryCode recipientCountryCode) {

        if (!this.departmentCountryAvailabilityService.isCountryAvailable(senderCountryCode)) {
            return Result.failure(ErrorCode.ORIGIN_DEPARTMENT_NOT_AVAILABLE);
        }

        if (!this.departmentCountryAvailabilityService.isCountryAvailable(recipientCountryCode)) {
            return Result.failure(ErrorCode.DESTINATION_DEPARTMENT_NOT_AVAILABLE);
        }

        return Result.success();
    }

    @Override
    @Transactional
    public void processShipmentDelivery(final ShipmentDeliveryCommand command) {
        final DeliveryStatus deliveryStatus = command.getDeliveryStatus();
        final ShipmentId shipmentId = command.getShipmentId();

        final Shipment shipment = this.shipmentRepository.findById(shipmentId);

        this.shipmentDeliveryStrategyResolver.resolve(deliveryStatus)
                .process(shipment)
                .ifPresent(event -> {
                    this.shipmentRepository.createOrUpdate(shipment);
                    this.domainEventPublisher.publish(event);
                });
    }

    @Override
    @Transactional
    public void cancel(final ShipmentId shipmentId) {
        final ShipmentWorkflowSettings settings = this.shipmentConfigurationServicePort
                .getCurrentOperatorShipmentConfiguration().workflowSettings();
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.cancel(settings, LocalDateTime.now());
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentCanceled(shipment.snapshot(), Instant.now()));
    }

    public void changeSenderTo(final ShipmentId shipmentId, final Party sender) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.changeSender(sender);
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentSenderChanged(shipment.snapshot(), Instant.now()));
    }

    public void changeRecipientTo(final ShipmentId shipmentId, final Party recipient) {
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
        this.domainEventPublisher.publish(new ShipmentRecipientChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void changePersonTo(final Party party, final PersonType personType, final ShipmentId shipmentId) {
        if (personType == PersonType.SENDER) {
            changeSenderTo(shipmentId, party);
        } else if (personType == PersonType.RECIPIENT) {
            changeRecipientTo(shipmentId, party);
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
			final ShipmentId shipmentId = ShipmentId.nextId();
            final OperatorShipmentConfiguration shipmentConfiguration =
                    this.shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration();
            final TrackingNumber trackingNumber = this.trackingNumberGenerationService.generate(
                    shipmentConfiguration.trackingNumberRule(), shipmentId);
            final Shipment newShipment = Shipment.parentShipment(shipmentId, shipment.getSender(),
                    shipment.getRecipient(), shipment.getShipmentId(),
                    shipment.getPrice(),
					shipment.getTargetDepartmentId(), shipment.getOriginDepartmentId(),
                    shipment.getSignatureRequired(), shipment.getShipmentPriority(), trackingNumber,
					shipmentConfiguration.workflowSettings().defaultStatus());
			this.changeShipmentTypeTo(request.shipmentId(), ShipmentType.CHILD, shipmentId);
			this.shipmentRepository.createOrUpdate(newShipment);
            this.domainEventPublisher.publish(new ShipmentCreated(shipment.snapshot(), Instant.now()));
        } else {
			this.changeShipmentTypeTo(request.shipmentId(), ShipmentType.PARENT, null);
			this.lockShipment(shipment.getShipmentRelatedId());
		}
    }

	@Override
	@Transactional
	public void changeShipmentStatusTo(final ShipmentStatusRequest request) {
		final Shipment shipment = this.shipmentRepository.findById(request.shipmentId());
		final ShipmentEvent event = this.shipmentStatusChangeStrategyResolver
                .resolve(request.shipmentStatus())
				.process(shipment);
		this.shipmentRepository.createOrUpdate(shipment);
		this.domainEventPublisher.publish(event);
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
    public ShipmentResult loadShipment(final ShipmentId shipmentId) {
        return this.shipmentResultFactory.create(this.find(shipmentId));
    }

    @Override
    public ShipmentResult loadShipment(final TrackingNumber trackingNumber) {
        return this.shipmentResultFactory.create(this.find(trackingNumber));
    }

    @Override
    public ShipmentRouteLog loadShipmentWithRouteLog(final ShipmentId shipmentId) {
        return this.shipmentResultFactory.createControlCenter(this.find(shipmentId));
    }

    @Override
    public ShipmentRouteLog loadShipmentWithRouteLog(final TrackingNumber trackingNumber) {
        return this.shipmentResultFactory.createControlCenter(this.find(trackingNumber));
    }

    @Override
    public boolean existsShipment(final ShipmentId shipmentId) {
        return this.shipmentRepository.exists(shipmentId);
    }

    private void logCreatedShipment(final Shipment shipment) {
        logger.info("Shipment {} has been created at {} with priority {}", shipment.getShipmentId().getValue(), shipment.getCreatedAt(),
                shipment.getShipmentPriority());
    }

    private Shipment find(final ShipmentId shipmentId) {
        return this.shipmentRepository.findById(shipmentId);
    }

    private Shipment find(final TrackingNumber trackingNumber) {
        return this.shipmentRepository.findByTrackingNumber(trackingNumber);
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
        this.domainEventPublisher.publish(new ShipmentTypeChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void lockShipment(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        shipment.lockShipment();
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentLocked(shipment.snapshot(), Instant.now()));
    }

    @Override
    public void redirectShipmentToSender(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        final ShipmentId redirectedShipmentId = shipment.getShipmentRelatedId();
        final OperatorShipmentConfiguration cfg = this.shipmentConfigurationServicePort
                .getCurrentOperatorShipmentConfiguration();
        final TrackingNumber trackingNumber = this.trackingNumberGenerationService.generate(
                cfg.workflowSettings(), cfg.trackingNumberRule(), redirectedShipmentId);
        final Shipment redirectedShipment = shipment.redirectToSender(redirectedShipmentId, trackingNumber,
                ExternalId.randomUUID(), cfg.workflowSettings());
        this.shipmentRepository.createOrUpdate(redirectedShipment);
        this.domainEventPublisher.publish(new ShipmentCreated(redirectedShipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void changeDestination(final ShipmentId shipmentId, final DepartmentCode destination) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        final DepartmentId targetDepartmentId = departmentServicePort.getDepartmentId(destination);
        shipment.changeTargetDepartment(targetDepartmentId);
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentDestinationChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void markReturned(final ShipmentId shipmentId) {
        final Shipment shipment = shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentReturned(ShipmentId.nextId());
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentReturned(shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void restoreAfterReturnCancellation(final ShipmentId shipmentId) {
        final Shipment shipment = shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentReturnCanceled();
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentStatusChanged(shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void notifyShipmentReturnCompleted(final ShipmentId shipmentId) {
        final Shipment shipment = shipmentRepository.findById(shipmentId);
        shipment.notifyShipmentReturnCompleted();
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentReturnedCompleted(shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void notifyShipmentReturnCanceled(final ShipmentId shipmentId) {
        final Shipment shipment = shipmentRepository.findById(shipmentId);
        shipment.markAsCanceledWithoutPolicy();
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentCanceled(shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void returnToSender(final ShipmentId shipmentId) {
        final Shipment shipment = this.shipmentRepository.findById(shipmentId);
        final ShipmentId returnedShipmentId = shipment.getShipmentRelatedId();
        final OperatorShipmentConfiguration cfg = this.shipmentConfigurationServicePort.getCurrentOperatorShipmentConfiguration();
        final TrackingNumber trackingNumber = this.trackingNumberGenerationService.generate(cfg.workflowSettings(),
                cfg.trackingNumberRule(), returnedShipmentId);
        final Shipment returnedShipment = shipment.returnToSender(returnedShipmentId, trackingNumber,
                cfg.workflowSettings(), ExternalId.randomUUID());
        this.shipmentRepository.createOrUpdate(returnedShipment);
        this.domainEventPublisher.publish(new ShipmentCreated(returnedShipment.snapshot(), Instant.now()));
    }
}
