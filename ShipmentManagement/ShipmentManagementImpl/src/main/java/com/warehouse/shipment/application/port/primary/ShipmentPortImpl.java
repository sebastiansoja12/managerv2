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
import com.warehouse.shipment.application.port.primary.result.ShipmentRerouteResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.application.port.secondary.*;
import com.warehouse.shipment.application.service.DepartmentCountryAvailabilityService;
import com.warehouse.shipment.application.service.ShipmentDepartmentResolutionService;
import com.warehouse.shipment.application.service.ShipmentResultFactory;
import com.warehouse.shipment.application.service.TrackingNumberGenerationService;
import com.warehouse.shipment.application.service.delivery.ShipmentDeliveryStrategyResolver;
import com.warehouse.shipment.application.service.status.ShipmentStatusChangeStrategyResolver;
import com.warehouse.shipment.domain.enumeration.PackagingType;
import com.warehouse.shipment.domain.enumeration.PersonType;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.enumeration.ShipmentSource;
import com.warehouse.shipment.domain.event.*;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.service.AddressValidationService;
import com.warehouse.shipment.domain.service.ShipmentStateValidatorService;
import com.warehouse.shipment.domain.service.ShipmentStateValidatorServiceImpl;
import com.warehouse.shipment.domain.vo.Address;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.ShipmentDeliveryPlan;
import com.warehouse.shipment.domain.vo.ShipmentOwnerContext;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.domain.vo.conf.ShipmentMetrics;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberPrefixMode;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

public class ShipmentPortImpl implements ShipmentPort {

    private final ShipmentRepository shipmentRepository;
    private final Logger logger;
    private final DepartmentCountryAvailabilityService departmentCountryAvailabilityService;
    private final ShipmentResultFactory shipmentResultFactory;
    private final TrackingNumberGenerationService trackingNumberGenerationService;
    private final ShipmentConfigurationPort shipmentConfigurationPort;
    private final ShipmentDeliveryStrategyResolver shipmentDeliveryStrategyResolver;
    private final ShipmentStatusChangeStrategyResolver shipmentStatusChangeStrategyResolver;
    private final DomainEventPublisher domainEventPublisher;
    private final ShipmentDepartmentResolutionService shipmentDepartmentResolutionService;
    private final AddressValidationService addressValidationService;
    private final PostalCodeServicePort postalCodeServicePort;
    private final OperatorServicePort operatorServicePort;
    private final ShipmentStateValidatorService shipmentStateValidatorService = new ShipmentStateValidatorServiceImpl();

    public ShipmentPortImpl(final ShipmentRepository shipmentRepository,
                            final Logger logger,
                            final DepartmentCountryAvailabilityService departmentCountryAvailabilityService,
                            final ShipmentResultFactory shipmentResultFactory,
                            final TrackingNumberGenerationService trackingNumberGenerationService,
                            final ShipmentConfigurationPort shipmentConfigurationPort,
                            final ShipmentDeliveryStrategyResolver shipmentDeliveryStrategyResolver,
                            final ShipmentStatusChangeStrategyResolver shipmentStatusChangeStrategyResolver,
                            final DomainEventPublisher domainEventPublisher,
                            final ShipmentDepartmentResolutionService shipmentDepartmentResolutionService,
                            final AddressValidationService addressValidationService,
                            final PostalCodeServicePort postalCodeServicePort,
                            final OperatorServicePort operatorServicePort) {
        this.shipmentRepository = shipmentRepository;
        this.logger = logger;
        this.departmentCountryAvailabilityService = departmentCountryAvailabilityService;
        this.shipmentResultFactory = shipmentResultFactory;
        this.trackingNumberGenerationService = trackingNumberGenerationService;
        this.shipmentConfigurationPort = shipmentConfigurationPort;
        this.shipmentDeliveryStrategyResolver = shipmentDeliveryStrategyResolver;
        this.shipmentStatusChangeStrategyResolver = shipmentStatusChangeStrategyResolver;
        this.domainEventPublisher = domainEventPublisher;
        this.shipmentDepartmentResolutionService = shipmentDepartmentResolutionService;
        this.addressValidationService = addressValidationService;
        this.postalCodeServicePort = postalCodeServicePort;
        this.operatorServicePort = operatorServicePort;
    }

    @Override
    @Transactional
    public Result<ShipmentCreateResponse, ErrorCode> ship(final ShipmentCreateCommand command) {
        final OperatorShipmentConfiguration shipmentConfiguration = currentConfiguration();

        final Result<Party, ErrorCode> senderWithCountry =
                assignCountry(command.getSender(), ErrorCode.SENDER_POSTAL_CODE_NOT_FOUND);
        if (senderWithCountry.isFailure()) {
            return Result.failure(senderWithCountry.getFailure());
        }

        final Result<Party, ErrorCode> recipientWithCountry =
                assignCountry(command.getRecipient(), ErrorCode.RECIPIENT_POSTAL_CODE_NOT_FOUND);
        if (recipientWithCountry.isFailure()) {
            return Result.failure(recipientWithCountry.getFailure());
        }

        final Party sender = senderWithCountry.getSuccess();
        final Party recipient = recipientWithCountry.getSuccess();

        final Result<Void, ErrorCode> addressValidation = this.addressValidationService.validateAddresses(
                Address.from(sender), Address.from(recipient), shipmentConfiguration.validationRules());
        if (addressValidation.isFailure()) {
            return Result.failure(addressValidation.getFailure());
        }

        final Result<Void, ErrorCode> countryValidation =
                validateCountries(sender.getCountryCode(), recipient.getCountryCode());
        if (countryValidation.isFailure()) {
            return Result.failure(countryValidation.getFailure());
        }

        final ShipmentMetrics shipmentMetrics = ShipmentMetrics.from(
                command.getDimensions(), command.getWeight(), command.getDeclaredValue());
        final Result<Void, String> limitationValidation = this.shipmentStateValidatorService
                .validateShipmentLimitations(shipmentConfiguration, shipmentMetrics);
        if (limitationValidation.isFailure()) {
            return Result.failure(ErrorCode.SHIPMENT_EXTENDED_LIMITATIONS);
        }

        final Result<DepartmentId, ErrorCode> targetDepartment =
                this.shipmentDepartmentResolutionService.resolveTargetDepartmentId(command);
        if (targetDepartment.isFailure()) {
            return Result.failure(targetDepartment.getFailure());
        }

        final Result<DepartmentId, ErrorCode> originDepartment =
                this.shipmentDepartmentResolutionService.resolveOriginDepartmentId(command);
        if (originDepartment.isFailure()) {
            return Result.failure(originDepartment.getFailure());
        }

        final PickupMethod pickupMethod = command.getPickupMethod();
        final ShipmentWorkflowSettings workflowSettings = shipmentConfiguration.workflowSettings();
        final ShipmentStatus initialStatus = pickupMethod.isPickupPointBased()
                ? ShipmentStatus.PLANNED
                : workflowSettings.defaultStatus();

        final ShipmentId shipmentId = ShipmentId.nextId();
        final TrackingNumber trackingNumber = requiresTemporaryTrackingNumber(command, shipmentConfiguration)
                ? this.trackingNumberGenerationService.temporary()
                : this.trackingNumberGenerationService.generate(workflowSettings,
                        shipmentConfiguration.trackingNumberRule(), shipmentId);

        final Shipment shipment = new Shipment(
                shipmentId,
                sender,
                recipient,
                null,
                command.getPrice(),
                false,
                command.getShipmentPriority(),
                trackingNumber,
                initialStatus,
                command.getDimensions(),
                command.getWeight(),
                command.getCustomerReference(),
                command.getContentDescription(),
                command.getDeclaredValue(),
                command.getServiceLevel() == null ? workflowSettings.defaultServiceLevel() : command.getServiceLevel(),
                command.getPackagingType(),
                command.getSource()
        );

        this.shipmentRepository.createOrUpdate(shipment);
        this.logger.info("Shipment {} has been created at {} with priority {}",
                shipment.getShipmentId().getValue(), shipment.getCreatedAt(), shipment.getShipmentPriority());

        final ShipmentDeliveryPlan deliveryPlan = new ShipmentDeliveryPlan(
                originDepartment.getSuccess(),
                targetDepartment.getSuccess(),
                pickupMethod,
                command.getDeliveryMethod(),
                pickupMethod.isPickupPointBased() ? command.getPickupPointId() : null,
                command.getDeliveryMethod().isPickupPointBased() ? command.getDeliveryPickupPointId() : null);

        this.domainEventPublisher.publish(new ShipmentCreated(shipment.snapshot(), deliveryPlan, Instant.now()));

        return Result.success(new ShipmentCreateResponse(shipment.getExternalShipmentId(),
                shipment.getTrackingNumber().value()));
    }

    @Override
    @Transactional
    public void processShipmentDelivery(final ShipmentDeliveryCommand command) {
        final DeliveryStatus deliveryStatus = command.getDeliveryStatus();
        final Shipment shipment = find(command.getShipmentId());

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
        final ShipmentWorkflowSettings settings = currentConfiguration().workflowSettings();
        final Shipment shipment = find(shipmentId);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.cancel(settings, LocalDateTime.now());
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentCanceled(previousSnapshot, shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void markLost(final ShipmentId shipmentId) {
        changeShipmentStatusTo(new ShipmentStatusRequest(shipmentId, ShipmentStatus.LOST));
    }

    @Override
    @Transactional
    public void returnAfterIncident(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.returnAfterIncident(ShipmentId.nextId());
        if (previousSnapshot.shipmentStatus() == shipment.getShipmentStatus()) {
            return;
        }
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentReturned(shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void cancelAfterIncident(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.cancelAfterIncident(LocalDateTime.now());
        if (previousSnapshot.shipmentStatus() == shipment.getShipmentStatus()) {
            return;
        }
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentCanceled(previousSnapshot, shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void repackAfterIncident(final ShipmentId shipmentId, final PackagingType packagingType, final Weight weight,
                                    final Dimensions dimensions) {
        final Shipment shipment = find(shipmentId);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.repack(packagingType, weight, dimensions);
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentChanged(previousSnapshot, shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void changePersonTo(final Party party, final PersonType personType, final ShipmentId shipmentId) {
        if (personType == PersonType.SENDER) {
            changeSenderTo(shipmentId, assignCountryOrThrow(party, ErrorCode.SENDER_POSTAL_CODE_NOT_FOUND));
        } else if (personType == PersonType.RECIPIENT) {
            changeRecipientTo(shipmentId, assignCountryOrThrow(party, ErrorCode.RECIPIENT_POSTAL_CODE_NOT_FOUND));
        }
    }

    private void changeSenderTo(final ShipmentId shipmentId, final Party sender) {
        final Shipment shipment = find(shipmentId);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.changeSender(sender);
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentSenderChanged(previousSnapshot, shipment.snapshot(),
                Instant.now()));
    }

    private void changeRecipientTo(final ShipmentId shipmentId, final Party recipient) {
        final Shipment shipment = find(shipmentId);
        final boolean destinationChanged = !shipment.recipientAddressMatches(recipient);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.changeRecipient(recipient);
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentRecipientChanged(previousSnapshot, shipment.snapshot(),
                Instant.now()));
        if (destinationChanged) {
            this.domainEventPublisher.publish(new ShipmentDestinationChanged(shipment.snapshot(), Instant.now()));
        }
    }

    @Override
    @Transactional
    public void changeShipmentTypeTo(final ChangeShipmentTypeRequest request) {
        final Shipment shipment = find(request.shipmentId());

        if (shipment.getShipmentType() == request.shipmentType()) {
            throw new RestException(400, "Shipment type cannot be changed to the same type");
        }

        final Result<Void, String> stateValidation = this.shipmentStateValidatorService.validateShipmentState(shipment);
        if (stateValidation.isFailure()) {
            throw new RestException(400, stateValidation.getFailure());
        }

        if (request.shipmentType() == ShipmentType.CHILD) {
            final ShipmentId parentShipmentId = ShipmentId.nextId();
            final OperatorShipmentConfiguration shipmentConfiguration = currentConfiguration();
            final TrackingNumber trackingNumber = this.trackingNumberGenerationService.generate(
                    shipmentConfiguration.trackingNumberRule(), parentShipmentId);
            final Shipment parentShipment = Shipment.parentShipment(parentShipmentId, shipment.getSender(),
                    shipment.getRecipient(), shipment.getShipmentId(), shipment.getPrice(),
                    shipment.getSignatureRequired(), shipment.getShipmentPriority(), trackingNumber,
                    shipmentConfiguration.workflowSettings().defaultStatus());
            changeShipmentTypeTo(request.shipmentId(), ShipmentType.CHILD, parentShipmentId);
            this.shipmentRepository.createOrUpdate(parentShipment);
            this.domainEventPublisher.publish(new ShipmentCreated(parentShipment.snapshot(), Instant.now()));
        } else {
            changeShipmentTypeTo(request.shipmentId(), ShipmentType.PARENT, null);
            lockShipment(shipment.getShipmentRelatedId());
        }
    }

    @Override
    @Transactional
    public void changeShipmentTypeTo(final ShipmentId shipmentId,
                                     final ShipmentType shipmentType,
                                     final ShipmentId relatedShipmentId) {
        final Shipment shipment = find(shipmentId);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        if (relatedShipmentId == null) {
            shipment.changeShipmentType(shipmentType);
        } else {
            shipment.changeShipmentTypeWithRelatedId(shipmentType, relatedShipmentId);
        }
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentTypeChanged(previousSnapshot, shipment.snapshot(),
                Instant.now()));
    }

    @Override
    @Transactional
    public void changeShipmentStatusTo(final ShipmentStatusRequest request) {
        final Shipment shipment = find(request.shipmentId());
        final ShipmentEvent event = this.shipmentStatusChangeStrategyResolver
                .resolve(request.shipmentStatus())
                .process(shipment);
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(event);
    }

    @Override
    @Transactional
    public void markDelivered(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        if (shipment.isFullyDelivered()) {
            return;
        }
        changeShipmentStatusTo(new ShipmentStatusRequest(shipmentId, ShipmentStatus.DELIVERY));
    }

    @Override
    public ShipmentResult loadShipment(final ShipmentId shipmentId) {
        return this.shipmentResultFactory.createWithDeliveryDetails(find(shipmentId));
    }

    @Override
    public ShipmentResult loadShipment(final TrackingNumber trackingNumber) {
        return this.shipmentResultFactory.create(find(trackingNumber));
    }

    @Override
    public ShipmentRouteLog loadShipmentWithRouteLog(final ShipmentId shipmentId) {
        return this.shipmentResultFactory.createControlCenter(find(shipmentId));
    }

    @Override
    public ShipmentRouteLog loadShipmentWithRouteLog(final TrackingNumber trackingNumber) {
        return this.shipmentResultFactory.createControlCenter(find(trackingNumber));
    }

    @Override
    public Optional<OperatorId> findOperator(final TrackingNumber trackingNumber) {
        return this.shipmentRepository.findOperatorIdByTrackingNumber(trackingNumber);
    }

    @Override
    public Optional<ShipmentResult> findSourceShipment(final ShipmentId shipmentId) {
        return this.shipmentRepository.findByShipmentRelatedId(shipmentId)
                .map(Shipment::snapshot)
                .map(ShipmentResult::new);
    }

    @Override
    public ShipmentRerouteResult loadShipmentForReroute(final TrackingNumber trackingNumber) {
        final Shipment shipment = find(trackingNumber);
        return new ShipmentRerouteResult(shipment.snapshot(), shipment.canBeRerouted());
    }

    @Override
    @Transactional
    public ShipmentRerouteResult reroute(final ShipmentRerouteCommand command) {
        final Party sender = assignCountryOrThrow(command.sender(), ErrorCode.SENDER_POSTAL_CODE_NOT_FOUND);
        final Party recipient = assignCountryOrThrow(command.recipient(), ErrorCode.RECIPIENT_POSTAL_CODE_NOT_FOUND);

        final Result<Void, ErrorCode> addressValidation = this.addressValidationService
                .validateAddresses(Address.from(sender), Address.from(recipient), null);
        if (addressValidation.isFailure()) {
            throw new RestException(400, addressValidation.getFailure().getMessage());
        }

        final Result<Void, ErrorCode> countryValidation =
                validateCountries(sender.getCountryCode(), recipient.getCountryCode());
        if (countryValidation.isFailure()) {
            throw new RestException(400, countryValidation.getFailure().getMessage());
        }

        final Shipment shipment = find(command.trackingNumber());
        final boolean destinationChanged = !shipment.recipientAddressMatches(recipient);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.reroute(sender, recipient);
        this.shipmentRepository.createOrUpdate(shipment);

        if (destinationChanged) {
            this.domainEventPublisher.publish(new ShipmentDestinationChanged(shipment.snapshot(), Instant.now()));
        }
        this.domainEventPublisher.publish(new ShipmentRerouted(previousSnapshot, shipment.snapshot(), Instant.now()));
        return new ShipmentRerouteResult(shipment.snapshot(), shipment.canBeRerouted());
    }

    @Override
    @Transactional
    public ShipmentResult collectShipment(final TrackingNumber trackingNumber) {
        final Shipment shipment = find(trackingNumber);
        shipment.notifyShipmentCollected();
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentCollected(shipment.snapshot(), Instant.now()));
        return this.shipmentResultFactory.create(shipment);
    }

    @Override
    @Transactional
    public ShipmentResult acceptShipmentInDepartment(final TrackingNumber trackingNumber) {
        final Shipment shipment = find(trackingNumber);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.acceptInDepartment();
        this.shipmentRepository.createOrUpdate(shipment);
        publishStatusChanged(previousSnapshot, shipment);
        return this.shipmentResultFactory.create(shipment);
    }

    @Override
    @Transactional
    public ShipmentResult handOverShipment(final ShipmentHandoverCommand command) {
        final Shipment shipment = find(command.trackingNumber());
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.handOver();
        if (previousSnapshot.shipmentStatus() == shipment.getShipmentStatus()) {
            return this.shipmentResultFactory.create(shipment);
        }
        this.shipmentRepository.createOrUpdate(shipment);
        publishStatusChanged(previousSnapshot, shipment);
        return this.shipmentResultFactory.create(shipment);
    }

    @Override
    @Transactional
    public ShipmentResult loadShipmentOnPallet(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        shipment.notifyShipmentLoadedOnPallet();
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentSent(shipment.snapshot(), Instant.now()));
        return this.shipmentResultFactory.create(shipment);
    }

    @Override
    @Transactional
    public ShipmentResult revertShipmentLoadingFromPallet(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        final ShipmentWorkflowSettings workflowSettings = currentConfiguration().workflowSettings();
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.revertLoadingFromPallet(workflowSettings);
        this.shipmentRepository.createOrUpdate(shipment);
        publishStatusChanged(previousSnapshot, shipment);
        return this.shipmentResultFactory.create(shipment);
    }

    @Override
    @Transactional
    public ShipmentResult unloadShipmentFromPallet(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.unloadFromPallet();
        this.shipmentRepository.createOrUpdate(shipment);
        publishStatusChanged(previousSnapshot, shipment);
        return this.shipmentResultFactory.create(shipment);
    }

    @Override
    public boolean existsShipment(final ShipmentId shipmentId) {
        return this.shipmentRepository.exists(shipmentId);
    }

    @Override
    @Transactional
    public void lockShipment(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        shipment.lockShipment();
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentLocked(shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void redirectShipmentToSender(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        final ShipmentId redirectedShipmentId = shipment.getShipmentRelatedId();
        final OperatorShipmentConfiguration shipmentConfiguration = currentConfiguration();
        final TrackingNumber trackingNumber = this.trackingNumberGenerationService.generate(
                shipmentConfiguration.workflowSettings(), shipmentConfiguration.trackingNumberRule(),
                redirectedShipmentId);
        final Shipment redirectedShipment = shipment.redirectToSender(redirectedShipmentId, trackingNumber,
                ExternalId.randomUUID(), shipmentConfiguration.workflowSettings());
        this.shipmentRepository.createOrUpdate(redirectedShipment);
        this.domainEventPublisher.publish(new ShipmentCreated(redirectedShipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void markReturned(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        shipment.notifyShipmentReturned(ShipmentId.nextId());
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentReturned(shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void markReturned(final ShipmentId shipmentId, final ShipmentId returnShipmentId) {
        final Shipment shipment = find(shipmentId);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.notifyShipmentReturned(returnShipmentId);
        this.shipmentRepository.createOrUpdate(shipment);
        publishStatusChanged(previousSnapshot, shipment);
    }

    @Override
    @Transactional
    public ShipmentId createReturnShipmentDraft(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        final ShipmentId returnShipmentId = ShipmentId.nextId();
        final OperatorShipmentConfiguration shipmentConfiguration = currentConfiguration();
        final TrackingNumber trackingNumber = this.trackingNumberGenerationService.generate(
                shipmentConfiguration.workflowSettings(), shipmentConfiguration.trackingNumberRule(),
                returnShipmentId);
        final Shipment returnShipment = shipment.prepareReturnShipmentDraft(returnShipmentId, trackingNumber,
                shipmentConfiguration.workflowSettings(), ExternalId.randomUUID());
        this.shipmentRepository.createOrUpdate(returnShipment);
        this.domainEventPublisher.publish(new ShipmentCreated(returnShipment.snapshot(), Instant.now()));
        return returnShipmentId;
    }

    @Override
    @Transactional
    public void restoreAfterReturnCancellation(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.notifyShipmentReturnCanceled();
        this.shipmentRepository.createOrUpdate(shipment);
        publishStatusChanged(previousSnapshot, shipment);
    }

    @Override
    @Transactional
    public void notifyShipmentReturnCompleted(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        shipment.notifyShipmentReturnCompleted();
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentReturnedCompleted(shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void notifyShipmentReturnCanceled(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.markAsCanceledWithoutPolicy();
        this.shipmentRepository.createOrUpdate(shipment);
        this.domainEventPublisher.publish(new ShipmentCanceled(previousSnapshot, shipment.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void returnToSender(final ShipmentId shipmentId) {
        final Shipment shipment = find(shipmentId);
        final ShipmentId returnedShipmentId = shipment.getShipmentRelatedId();
        final OperatorShipmentConfiguration shipmentConfiguration = currentConfiguration();
        final TrackingNumber trackingNumber = this.trackingNumberGenerationService.generate(
                shipmentConfiguration.workflowSettings(), shipmentConfiguration.trackingNumberRule(),
                returnedShipmentId);
        final Shipment returnedShipment = shipment.returnToSender(returnedShipmentId, trackingNumber,
                shipmentConfiguration.workflowSettings(), ExternalId.randomUUID());
        this.shipmentRepository.createOrUpdate(returnedShipment);
        this.domainEventPublisher.publish(new ShipmentCreated(returnedShipment.snapshot(), Instant.now()));
    }

    @Override
    public boolean hasAnyShipment() {
        return this.shipmentRepository.existsAny();
    }

    @Override
    public boolean isOperatorActive(final OperatorId operatorId) {
        return this.operatorServicePort.isActive(operatorId);
    }

    @Override
    public ShipmentOwnerContext findContext(final TrackingNumber trackingNumber) {
        return this.shipmentRepository.findContext(trackingNumber);
    }

    private Shipment find(final ShipmentId shipmentId) {
        return this.shipmentRepository.findById(shipmentId);
    }

    private Shipment find(final TrackingNumber trackingNumber) {
        return this.shipmentRepository.findByTrackingNumber(trackingNumber);
    }

    private OperatorShipmentConfiguration currentConfiguration() {
        return this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration();
    }

    private void publishStatusChanged(final ShipmentSnapshot previousSnapshot, final Shipment shipment) {
        this.domainEventPublisher.publish(new ShipmentStatusChanged(previousSnapshot, shipment.snapshot(),
                Instant.now()));
    }

    private boolean requiresTemporaryTrackingNumber(final ShipmentCreateCommand command,
                                                    final OperatorShipmentConfiguration shipmentConfiguration) {
        return command.getSource() == ShipmentSource.EXTERNAL
                && shipmentConfiguration.trackingNumberRule().prefixMode() == TrackingNumberPrefixMode.DEPARTMENT_CODE;
    }

    private Result<Party, ErrorCode> assignCountry(final Party party, final ErrorCode postalCodeNotFound) {
        return this.postalCodeServicePort.findCountryCode(party.getPostalCode(), party.getCity())
                .map(countryCode -> Result.<Party, ErrorCode>success(party.withCountryCode(countryCode)))
                .orElseGet(() -> Result.failure(postalCodeNotFound));
    }

    private Party assignCountryOrThrow(final Party party, final ErrorCode postalCodeNotFound) {
        final Result<Party, ErrorCode> partyWithCountry = assignCountry(party, postalCodeNotFound);
        if (partyWithCountry.isFailure()) {
            throw new RestException(400, partyWithCountry.getFailure().getMessage());
        }
        return partyWithCountry.getSuccess();
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
}
