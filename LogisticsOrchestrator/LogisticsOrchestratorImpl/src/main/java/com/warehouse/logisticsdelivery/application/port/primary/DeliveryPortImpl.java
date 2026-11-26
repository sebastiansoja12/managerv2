package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.logisticsdelivery.application.port.primary.command.AssignDeliverySupplierCommand;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryRelation;
import com.warehouse.logisticsdelivery.application.port.primary.command.AttachDeliveryStepSignatureCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ChangeDeliveryConditionsCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ChangeDeliveryRouteCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ChangePalletDeliveryRouteCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CommentDeliveryStepCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CorrectDeliveryStepStatusCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ReassignDeliveryStepCarrierCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RegisterDeliveryAttemptCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CompleteDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDerivedDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreatePalletDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RecordDeliveryAttemptCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RevertPalletDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.UnloadPalletDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.SignatureRepository;
import com.warehouse.logisticsdelivery.application.service.DeliveryChangeService;
import com.warehouse.logisticsdelivery.application.service.DeliveryCompletionService;
import com.warehouse.logisticsdelivery.application.service.DeliveryDestinationResolver;
import com.warehouse.logisticsdelivery.application.service.DeliveryFinder;
import com.warehouse.logisticsdelivery.application.service.DeliveryRelationResolver;
import com.warehouse.logisticsdelivery.application.service.PalletDeliveryService;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.event.DeliveryAttemptRecorded;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCanceled;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCompleted;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCreated;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import com.warehouse.logisticsdelivery.domain.exception.DestinationDepartmentNotResolvedException;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.model.Signature;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryStepSnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;
import com.warehouse.logisticsdelivery.domain.vo.RecipientAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DeliveryPortImpl implements DeliveryPort {

    private static final Logger log = LoggerFactory.getLogger(DeliveryPortImpl.class);

    private final DeliveryRepository deliveryRepository;

    private final ShipmentServicePort shipmentServicePort;

    private final OperatorContextProvider operatorContextProvider;

    private final SignatureRepository signatureRepository;

    private final DeliveryFinder deliveryFinder;

    private final DeliveryChangeService deliveryChangeService;

    private final DeliveryDestinationResolver deliveryDestinationResolver;

    private final DeliveryRelationResolver deliveryRelationResolver;

    private final DeliveryCompletionService deliveryCompletionService;

    private final PalletDeliveryService palletDeliveryService;

    public DeliveryPortImpl(final DeliveryRepository deliveryRepository,
                            final ShipmentServicePort shipmentServicePort,
                            final OperatorContextProvider operatorContextProvider,
                            final SignatureRepository signatureRepository,
                            final DeliveryFinder deliveryFinder,
                            final DeliveryChangeService deliveryChangeService,
                            final DeliveryDestinationResolver deliveryDestinationResolver,
                            final DeliveryRelationResolver deliveryRelationResolver,
                            final DeliveryCompletionService deliveryCompletionService,
                            final PalletDeliveryService palletDeliveryService) {
        this.deliveryRepository = deliveryRepository;
        this.shipmentServicePort = shipmentServicePort;
        this.operatorContextProvider = operatorContextProvider;
        this.signatureRepository = signatureRepository;
        this.deliveryFinder = deliveryFinder;
        this.deliveryChangeService = deliveryChangeService;
        this.deliveryDestinationResolver = deliveryDestinationResolver;
        this.deliveryRelationResolver = deliveryRelationResolver;
        this.deliveryCompletionService = deliveryCompletionService;
        this.palletDeliveryService = palletDeliveryService;
    }

    @Override
    @Transactional
    public void createDelivery(final CreateDeliveryCommand command) {
        final DeliveryTarget target = DeliveryTarget.shipment(command.shipmentId());
        this.deliveryFinder.ensureDoesNotExist(target);
        final DepartmentId destinationDepartmentId = command.destinationDepartmentId() == null
                && command.method() == DeliveryMethod.COURIER
                ? this.deliveryDestinationResolver.resolveForShipment(command.shipmentId()).orElse(null)
                : command.destinationDepartmentId();
        this.deliveryChangeService.save(new Delivery(target, command.shipmentId(), DeliveryType.OUTBOUND,
                command.originDepartmentId(), destinationDepartmentId, command.pickupMethod(), command.method(),
                command.pickupPointId(), command.deliveryPickupPointId(), command.signatureId(),
                command.signatureRequired(), command.userId()), DeliveryCreated::new);
    }

    @Override
    @Transactional
    public void createDerivedDelivery(final CreateDerivedDeliveryCommand command) {
        if (this.deliveryFinder.hasCurrentDelivery(DeliveryTarget.shipment(command.shipmentId()))) {
            return;
        }
        this.shipmentServicePort.findSourceShipment(command.shipmentId()).ifPresentOrElse(
                sourceShipment -> this.deliveryChangeService.save(Delivery.derivedFrom(
                        this.deliveryFinder.getShipmentDelivery(sourceShipment.shipmentId()), command.shipmentId(),
                        sourceShipment.derivationType(), command.signatureRequired(), command.userId()),
                        DeliveryCreated::new),
                () -> log.info("Shipment {} is not linked to a source shipment yet, delivery will be derived once linked",
                        command.shipmentId().getValue()));
    }

    @Override
    @Transactional
    public void createPalletDelivery(final CreatePalletDeliveryCommand command) {
        final DeliveryTarget target = DeliveryTarget.pallet(command.palletId());
        final Delivery palletDelivery = this.deliveryFinder.findOutbound(target)
                .map(delivery -> this.palletDeliveryService.reopen(delivery, command.palletId(),
                        command.originDepartmentId(), command.destinationDepartmentId()))
                .orElseGet(() -> {
                    final Delivery delivery = Delivery.forPallet(target, command.originDepartmentId(),
                            command.destinationDepartmentId(), command.userId());
                    this.deliveryChangeService.save(delivery, DeliveryCreated::new);
                    log.info("Pallet delivery {} created for pallet {}", delivery.getDeliveryId().getId(),
                            command.palletId());
                    return delivery;
                });
        command.shipmentIds().forEach(shipmentId -> this.deliveryFinder.findActiveShipmentDelivery(shipmentId)
                .ifPresent(delivery -> {
                    delivery.replaceByPalletDelivery(palletDelivery.getDeliveryId(), command.userId());
                    this.deliveryChangeService.save(delivery, DeliveryCanceled::new);
                }));
    }

    @Override
    @Transactional
    public void revertPalletDelivery(final RevertPalletDeliveryCommand command) {
        this.deliveryFinder.findOutbound(DeliveryTarget.pallet(command.palletId()))
                .filter(Delivery::isActive)
                .ifPresent(palletDelivery -> {
                    this.deliveryChangeService.cancel(palletDelivery);
                    this.palletDeliveryService.continueReplacedDeliveries(palletDelivery, command.shipmentIds(),
                            replaced -> Delivery.continuedAfterPalletLoadingReverted(replaced, command.userId()));
                    log.info("Pallet delivery {} reverted for pallet {}", palletDelivery.getDeliveryId().getId(),
                            command.palletId());
                });
    }

    @Override
    @Transactional
    public void unloadPalletDelivery(final UnloadPalletDeliveryCommand command) {
        this.deliveryFinder.findOutbound(DeliveryTarget.pallet(command.palletId().value()))
                .filter(Delivery::isActive)
                .ifPresent(palletDelivery -> {
                    palletDelivery.complete(palletDelivery.getSupplierId(), command.userId(),
                            command.unloadDepartmentId());
                    this.deliveryChangeService.save(palletDelivery, DeliveryCompleted::new);
                    this.palletDeliveryService.continueReplacedDeliveries(palletDelivery, command.shipmentIds(),
                            replaced -> Delivery.continuedAfterPalletUnloading(replaced,
                                    command.unloadDepartmentId(), command.userId()));
                    log.info("Pallet delivery {} completed after unloading pallet {}",
                            palletDelivery.getDeliveryId().getId(), command.palletId().value());
                });
    }

    @Override
    @Transactional
    public void changePalletDeliveryRoute(final ChangePalletDeliveryRouteCommand command) {
        this.deliveryFinder.findOutbound(DeliveryTarget.pallet(command.palletId().value()))
                .filter(Delivery::isActive)
                .ifPresent(palletDelivery -> {
                    palletDelivery.changeDepartments(command.originDepartmentId(),
                            command.destinationDepartmentId());
                    this.deliveryChangeService.save(palletDelivery, DeliveryModified::new);
                    log.info("Pallet delivery {} route changed for pallet {}",
                            palletDelivery.getDeliveryId().getId(), command.palletId().value());
                });
    }

    @Override
    @Transactional
    public void completeDelivery(final CompleteDeliveryCommand command) {
        this.deliveryFinder.findActiveShipmentDelivery(command.shipmentId()).ifPresent(delivery -> {
            delivery.complete(command.supplierId(), command.userId(), command.departmentId());
            this.deliveryChangeService.save(delivery, DeliveryCompleted::new);
        });
    }

    @Override
    @Transactional
    public DeliverySnapshot recordAttempt(final RecordDeliveryAttemptCommand command) {
        final Delivery delivery = this.deliveryFinder.getCurrent(command.target(), command.type());
        delivery.recordAttempt(command.deliveryStatus(), command.userId(), command.supplierId(),
                command.departmentId(), command.vehicleId(), command.comment(), command.token());
        return this.deliveryChangeService.save(delivery, DeliveryAttemptRecorded::new);
    }

    @Override
    @Transactional
    public void changeRecipientAddress(final ShipmentId shipmentId, final RecipientAddress recipientAddress) {
        this.deliveryFinder.findActiveShipmentDelivery(shipmentId).ifPresent(delivery -> {
            final DeliverySnapshot previousSnapshot = delivery.snapshot();
            delivery.changeRecipientAddress(recipientAddress, this.deliveryDestinationResolver::resolve);
            this.deliveryChangeService.saveChanged(delivery, previousSnapshot, DeliveryModified::new);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryRelation> findRelatedDeliveries(final DeliveryId deliveryId) {
        return this.deliveryRelationResolver.resolve(this.deliveryFinder.get(deliveryId));
    }

    @Override
    @Transactional
    public DeliverySnapshot assignSupplier(final AssignDeliverySupplierCommand command) {
        final Delivery delivery = this.deliveryFinder.get(command.deliveryId());
        delivery.assignSupplier(command.supplierId(), command.vehicleId());
        return this.deliveryChangeService.save(delivery, DeliveryModified::new);
    }

    @Override
    @Transactional
    public void assignShipmentSupplier(final ShipmentId shipmentId, final SupplierId supplierId,
                                       final VehicleId vehicleId) {
        this.deliveryFinder.findActiveShipmentDelivery(shipmentId)
                .ifPresent(delivery -> {
                    final DeliverySnapshot previousSnapshot = delivery.snapshot();
                    delivery.assignSupplier(supplierId, vehicleId);
                    this.deliveryChangeService.saveChanged(delivery, previousSnapshot, DeliveryModified::new);
                });
    }

    @Override
    @Transactional
    public DeliverySnapshot unassignSupplier(final DeliveryId deliveryId) {
        final Delivery delivery = this.deliveryFinder.get(deliveryId);
        final DeliverySnapshot previousSnapshot = delivery.snapshot();
        delivery.unassignSupplier();
        return this.deliveryChangeService.saveChanged(delivery, previousSnapshot, DeliveryModified::new);
    }

    @Override
    @Transactional
    public DeliverySnapshot changeRoute(final ChangeDeliveryRouteCommand command) {
        final Delivery delivery = this.deliveryFinder.get(command.deliveryId());
        delivery.changeRoute(command.originDepartmentId(), command.destinationDepartmentId(),
                command.pickupMethod(), command.pickupPointId(), command.deliveryPickupPointId());
        return this.deliveryChangeService.save(delivery, DeliveryModified::new);
    }

    @Override
    @Transactional
    public DeliverySnapshot resolveDestinationDepartment(final DeliveryId deliveryId) {
        final Delivery delivery = this.deliveryFinder.get(deliveryId);
        final DepartmentId destinationDepartmentId = this.deliveryDestinationResolver
                .resolveForShipment(delivery.getShipmentId())
                .orElseThrow(() -> new DestinationDepartmentNotResolvedException(
                        "Destination department of delivery " + deliveryId.getId() + " could not be resolved"));
        delivery.assignDestinationDepartment(destinationDepartmentId);
        return this.deliveryChangeService.save(delivery, DeliveryModified::new);
    }

    @Override
    @Transactional
    public DeliverySnapshot changeConditions(final ChangeDeliveryConditionsCommand command) {
        final Delivery delivery = this.deliveryFinder.get(command.deliveryId());
        delivery.changeConditions(command.method(), command.signatureRequired(), command.instructions(),
                command.plannedAt());
        return this.deliveryChangeService.save(delivery, DeliveryModified::new);
    }

    @Override
    @Transactional
    public DeliverySnapshot registerAttempt(final RegisterDeliveryAttemptCommand command) {
        final Delivery delivery = this.deliveryFinder.get(command.deliveryId());
        delivery.recordAttempt(command.deliveryStatus(), this.operatorContextProvider.currentUserId(),
                delivery.getSupplierId(), this.operatorContextProvider.currentDepartmentId(), delivery.getVehicleId(),
                command.comment(), null);
        return this.deliveryChangeService.save(delivery, DeliveryAttemptRecorded::new);
    }

    @Override
    @Transactional
    public DeliverySnapshot finishDelivery(final DeliveryId deliveryId) {
        return this.deliveryCompletionService.finish(this.deliveryFinder.get(deliveryId), null);
    }

    @Override
    @Transactional
    public DeliverySnapshot completeShipmentDelivery(final ShipmentId shipmentId, final boolean recipientSigned,
                                                     final String recipientName, final String signature) {
        final Delivery delivery = this.deliveryFinder.getShipmentDelivery(shipmentId);
        delivery.ensureRecipientSignature(recipientSigned);
        return this.deliveryCompletionService.finish(delivery, recipientSigned
                ? this.deliveryCompletionService.createRecipientSignature(recipientName, signature)
                : null);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Signature> findSignature(final DeliveryId deliveryId) {
        return this.deliveryFinder.get(deliveryId).snapshot().deliverySteps().reversed().stream()
                .map(DeliveryStepSnapshot::signatureId)
                .filter(Objects::nonNull)
                .findFirst()
                .flatMap(this.signatureRepository::findById);
    }

    @Override
    @Transactional
    public DeliverySnapshot commentStep(final CommentDeliveryStepCommand command) {
        final Delivery delivery = this.deliveryFinder.get(command.deliveryId());
        delivery.commentStep(command.stepNumber(), command.comment());
        return this.deliveryChangeService.save(delivery, DeliveryModified::new);
    }

    @Override
    @Transactional
    public DeliverySnapshot correctStepStatus(final CorrectDeliveryStepStatusCommand command) {
        final Delivery delivery = this.deliveryFinder.get(command.deliveryId());
        delivery.correctStepStatus(command.stepNumber(), command.deliveryStatus());
        return this.deliveryChangeService.save(delivery, DeliveryModified::new);
    }

    @Override
    @Transactional
    public DeliverySnapshot attachStepSignature(final AttachDeliveryStepSignatureCommand command) {
        final Delivery delivery = this.deliveryFinder.get(command.deliveryId());
        delivery.attachStepSignature(command.stepNumber(), command.signatureId());
        return this.deliveryChangeService.save(delivery, DeliveryModified::new);
    }

    @Override
    @Transactional
    public DeliverySnapshot reassignStepCarrier(final ReassignDeliveryStepCarrierCommand command) {
        final Delivery delivery = this.deliveryFinder.get(command.deliveryId());
        delivery.reassignStepCarrier(command.stepNumber(), command.supplierId(), command.vehicleId());
        return this.deliveryChangeService.save(delivery, DeliveryModified::new);
    }

    @Override
    @Transactional
    public void cancelDelivery(final DeliveryId deliveryId) {
        final Delivery delivery = this.deliveryFinder.get(deliveryId);
        if (delivery.getStatus() != DeliveryLifecycleStatus.CANCELED) {
            this.deliveryChangeService.cancel(delivery);
        }
    }

    @Override
    @Transactional
    public void cancelShipmentDeliveries(final ShipmentId shipmentId) {
        this.deliveryRepository.findByTarget(DeliveryTarget.shipment(shipmentId)).stream()
                .filter(Delivery::isActive)
                .forEach(delivery -> {
                    this.deliveryChangeService.cancel(delivery);
                    log.info("Delivery {} canceled for shipment {}", delivery.getDeliveryId().getId(),
                            shipmentId.getValue());
                });
    }

    @Override
    public List<DeliverySnapshot> findRecentDeliveries(final int offset, final int limit) {
        return this.deliveryRepository.findRecent(offset, limit).stream().map(Delivery::snapshot).toList();
    }

    @Override
    public List<DeliverySnapshot> findDeliveriesByDepartment(final DepartmentId departmentId, final int offset,
                                                             final int limit) {
        return this.deliveryRepository.findByDepartment(departmentId, offset, limit).stream()
                .map(Delivery::snapshot)
                .toList();
    }

    @Override
    public List<DeliverySnapshot> findDeliveriesBySupplier(final SupplierId supplierId, final int offset,
                                                           final int limit) {
        return this.deliveryRepository.findBySupplier(supplierId, offset, limit).stream()
                .map(Delivery::snapshot)
                .toList();
    }

    @Override
    public DeliverySnapshot findDelivery(final DeliveryId deliveryId) {
        return this.deliveryFinder.get(deliveryId).snapshot();
    }

    @Override
    public DeliverySnapshot findDeliveryByShipmentId(final ShipmentId shipmentId) {
        return this.deliveryFinder.getShipmentDelivery(shipmentId).snapshot();
    }

    @Override
    public DeliverySnapshot findDeliveryByPalletId(final String palletId) {
        return this.deliveryFinder.getPalletDelivery(palletId).snapshot();
    }
}
