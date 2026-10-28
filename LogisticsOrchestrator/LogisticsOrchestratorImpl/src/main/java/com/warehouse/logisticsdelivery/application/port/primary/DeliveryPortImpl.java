package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.logisticsdelivery.application.port.primary.command.AssignDeliverySupplierCommand;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryRelation;
import com.warehouse.logisticsdelivery.application.port.primary.command.AttachDeliveryStepSignatureCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ChangeDeliveryConditionsCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ChangeDeliveryRouteCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CommentDeliveryStepCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CorrectDeliveryStepStatusCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ReassignDeliveryStepCarrierCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RegisterDeliveryAttemptCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CompleteDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDerivedDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreatePalletDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RecordDeliveryAttemptCommand;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.SignatureRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.SourceShipment;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryRelationType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.enumeration.SignatureMethod;
import com.warehouse.logisticsdelivery.domain.event.DeliveryAttemptRecorded;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCanceled;
import com.warehouse.logisticsdelivery.domain.event.DeliveryChanged;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCompleted;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCreated;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryAlreadyExistsException;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryModificationException;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryNotFoundException;
import com.warehouse.logisticsdelivery.domain.exception.SourceShipmentNotFoundException;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.model.Signature;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryStepSnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class DeliveryPortImpl implements DeliveryPort {

    private static final Logger log = LoggerFactory.getLogger(DeliveryPortImpl.class);

    private final DeliveryRepository deliveryRepository;

    private final ShipmentServicePort shipmentServicePort;

    private final DomainEventPublisher domainEventPublisher;

    private final OperatorContextProvider operatorContextProvider;

    private final SignatureRepository signatureRepository;

    public DeliveryPortImpl(final DeliveryRepository deliveryRepository,
                            final ShipmentServicePort shipmentServicePort,
                            final DomainEventPublisher domainEventPublisher,
                            final OperatorContextProvider operatorContextProvider,
                            final SignatureRepository signatureRepository) {
        this.deliveryRepository = deliveryRepository;
        this.shipmentServicePort = shipmentServicePort;
        this.domainEventPublisher = domainEventPublisher;
        this.operatorContextProvider = operatorContextProvider;
        this.signatureRepository = signatureRepository;
    }

    @Override
    @Transactional
    public void createDelivery(final CreateDeliveryCommand command) {
        final DeliveryTarget target = DeliveryTarget.shipment(command.shipmentId());
        ensureDeliveryDoesNotExist(target);
        save(new Delivery(target, command.shipmentId(), DeliveryType.OUTBOUND, command.originDepartmentId(),
                command.destinationDepartmentId(), command.pickupMethod(), command.method(), command.pickupPointId(),
                command.deliveryPickupPointId(), command.signatureId(), command.signatureRequired(),
                command.userId()), DeliveryCreated::new);
    }

    @Override
    @Transactional
    public void createDerivedDelivery(final CreateDerivedDeliveryCommand command) {
        ensureDeliveryDoesNotExist(DeliveryTarget.shipment(command.shipmentId()));
        final SourceShipment sourceShipment = this.shipmentServicePort.findSourceShipment(command.shipmentId())
                .orElseThrow(() -> new SourceShipmentNotFoundException("Source shipment for shipment "
                        + command.shipmentId().getValue() + " was not found"));
        final Delivery source = findShipmentDelivery(sourceShipment.shipmentId());
        save(Delivery.derivedFrom(source, command.shipmentId(), sourceShipment.derivationType(),
                command.signatureRequired(), command.userId()), DeliveryCreated::new);
    }

    @Override
    @Transactional
    public void createPalletDelivery(final CreatePalletDeliveryCommand command) {
        final DeliveryTarget target = DeliveryTarget.pallet(command.palletId());
        final Delivery palletDelivery = findOutbound(target).orElseGet(() -> {
            final Delivery delivery = Delivery.forPallet(target, command.userId());
            save(delivery, DeliveryCreated::new);
            log.info("Pallet delivery {} created for pallet {}", delivery.getDeliveryId().getId(), command.palletId());
            return delivery;
        });
        command.shipmentIds().forEach(shipmentId -> findOutbound(DeliveryTarget.shipment(shipmentId))
                .filter(delivery -> delivery.replaceByPalletDelivery(palletDelivery.getDeliveryId(), command.userId()))
                .ifPresent(delivery -> save(delivery, DeliveryCanceled::new)));
    }

    @Override
    @Transactional
    public void completeDelivery(final CompleteDeliveryCommand command) {
        findActiveShipmentDelivery(command.shipmentId()).ifPresent(delivery -> {
            delivery.complete(command.supplierId(), command.userId(), command.departmentId());
            save(delivery, DeliveryCompleted::new);
        });
    }

    @Override
    @Transactional
    public DeliverySnapshot recordAttempt(final RecordDeliveryAttemptCommand command) {
        final Delivery delivery = this.deliveryRepository.findByTargetAndType(command.target(), command.type())
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery for target " + command.target().id()
                        + " was not found"));
        delivery.recordAttempt(command.deliveryStatus(), command.userId(), command.supplierId(),
                command.departmentId(), command.vehicleId(), command.comment(), command.token());
        return save(delivery, DeliveryAttemptRecorded::new);
    }

    @Override
    @Transactional
    public void changeDestinationDepartment(final ShipmentId shipmentId, final DepartmentId destinationDepartmentId) {
        findActiveShipmentDelivery(shipmentId)
                .filter(delivery -> delivery.changeDestinationDepartment(destinationDepartmentId))
                .ifPresent(delivery -> save(delivery, DeliveryModified::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryRelation> findRelatedDeliveries(final DeliveryId deliveryId) {
        final Delivery delivery = find(deliveryId);
        final List<DeliveryRelation> relations = new ArrayList<>();
        final ShipmentId shipmentId = delivery.getShipmentId();
        if (shipmentId != null) {
            final Optional<ShipmentId> sourceShipmentId = this.shipmentServicePort.findSourceShipment(shipmentId)
                    .map(SourceShipment::shipmentId);
            sourceShipmentId.ifPresent(id -> addShipmentRelations(relations, DeliveryRelationType.SOURCE, id));
            this.shipmentServicePort.findDerivedShipment(shipmentId)
                    .filter(id -> !id.equals(shipmentId) && !sourceShipmentId.map(id::equals).orElse(false))
                    .ifPresent(id -> addShipmentRelations(relations, DeliveryRelationType.DERIVED, id));
        }
        Optional.ofNullable(delivery.getReplacedByDeliveryId())
                .flatMap(this.deliveryRepository::findById)
                .ifPresent(replacing -> relations.add(new DeliveryRelation(DeliveryRelationType.REPLACED_BY,
                        replacing.snapshot())));
        this.deliveryRepository.findReplacedBy(deliveryId).forEach(replaced ->
                relations.add(new DeliveryRelation(DeliveryRelationType.REPLACES, replaced.snapshot())));
        return relations;
    }

    @Override
    @Transactional
    public DeliverySnapshot assignSupplier(final AssignDeliverySupplierCommand command) {
        final Delivery delivery = find(command.deliveryId());
        return saveModifiedWhen(delivery, delivery.assignSupplier(command.supplierId(), command.vehicleId()));
    }

    @Override
    @Transactional
    public void assignShipmentSupplier(final ShipmentId shipmentId, final SupplierId supplierId,
                                       final VehicleId vehicleId) {
        findActiveShipmentDelivery(shipmentId)
                .filter(delivery -> delivery.assignSupplier(supplierId, vehicleId))
                .ifPresent(delivery -> save(delivery, DeliveryModified::new));
    }

    @Override
    @Transactional
    public DeliverySnapshot unassignSupplier(final DeliveryId deliveryId) {
        final Delivery delivery = find(deliveryId);
        return saveModifiedWhen(delivery, delivery.unassignSupplier());
    }

    @Override
    @Transactional
    public DeliverySnapshot changeRoute(final ChangeDeliveryRouteCommand command) {
        final Delivery delivery = find(command.deliveryId());
        return saveModifiedWhen(delivery, delivery.changeRoute(command.originDepartmentId(),
                command.destinationDepartmentId(), command.pickupMethod(), command.pickupPointId(),
                command.deliveryPickupPointId()));
    }

    @Override
    @Transactional
    public DeliverySnapshot changeConditions(final ChangeDeliveryConditionsCommand command) {
        final Delivery delivery = find(command.deliveryId());
        return saveModifiedWhen(delivery, delivery.changeConditions(command.method(), command.signatureRequired(),
                command.instructions(), command.plannedAt()));
    }

    @Override
    @Transactional
    public DeliverySnapshot registerAttempt(final RegisterDeliveryAttemptCommand command) {
        final Delivery delivery = find(command.deliveryId());
        delivery.recordAttempt(command.deliveryStatus(), this.operatorContextProvider.currentUserId(),
                delivery.getSupplierId(), this.operatorContextProvider.currentDepartmentId(), delivery.getVehicleId(),
                command.comment(), null);
        return save(delivery, DeliveryAttemptRecorded::new);
    }

    @Override
    @Transactional
    public DeliverySnapshot finishDelivery(final DeliveryId deliveryId) {
        return finish(find(deliveryId), null);
    }

    @Override
    @Transactional
    public DeliverySnapshot completeShipmentDelivery(final ShipmentId shipmentId, final boolean recipientSigned,
                                                     final String recipientName, final String signature) {
        final Delivery delivery = findShipmentDelivery(shipmentId);
        delivery.ensureRecipientSignature(recipientSigned);
        return finish(delivery, recipientSigned ? createSignature(recipientName, signature) : null);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Signature> findSignature(final DeliveryId deliveryId) {
        return find(deliveryId).snapshot().deliverySteps().reversed().stream()
                .map(DeliveryStepSnapshot::signatureId)
                .filter(Objects::nonNull)
                .findFirst()
                .flatMap(this.signatureRepository::findById);
    }

    @Override
    @Transactional
    public DeliverySnapshot commentStep(final CommentDeliveryStepCommand command) {
        final Delivery delivery = find(command.deliveryId());
        return saveModifiedWhen(delivery, delivery.commentStep(command.stepNumber(), command.comment()));
    }

    @Override
    @Transactional
    public DeliverySnapshot correctStepStatus(final CorrectDeliveryStepStatusCommand command) {
        final Delivery delivery = find(command.deliveryId());
        return saveModifiedWhen(delivery, delivery.correctStepStatus(command.stepNumber(), command.deliveryStatus()));
    }

    @Override
    @Transactional
    public DeliverySnapshot attachStepSignature(final AttachDeliveryStepSignatureCommand command) {
        final Delivery delivery = find(command.deliveryId());
        return saveModifiedWhen(delivery, delivery.attachStepSignature(command.stepNumber(), command.signatureId()));
    }

    @Override
    @Transactional
    public DeliverySnapshot reassignStepCarrier(final ReassignDeliveryStepCarrierCommand command) {
        final Delivery delivery = find(command.deliveryId());
        return saveModifiedWhen(delivery, delivery.reassignStepCarrier(command.stepNumber(), command.supplierId(),
                command.vehicleId()));
    }

    @Override
    @Transactional
    public void cancelDelivery(final DeliveryId deliveryId) {
        final Delivery delivery = find(deliveryId);
        if (delivery.getStatus() != DeliveryLifecycleStatus.CANCELED) {
            cancel(delivery);
        }
    }

    @Override
    @Transactional
    public void cancelShipmentDeliveries(final ShipmentId shipmentId) {
        this.deliveryRepository.findByTarget(DeliveryTarget.shipment(shipmentId)).stream()
                .filter(Delivery::isActive)
                .forEach(delivery -> {
                    cancel(delivery);
                    log.info("Delivery {} canceled for canceled shipment {}", delivery.getDeliveryId().getId(),
                            shipmentId.getValue());
                });
    }

    @Override
    public List<DeliverySnapshot> findRecentDeliveries(final int offset, final int limit) {
        return snapshots(this.deliveryRepository.findRecent(offset, limit));
    }

    @Override
    public List<DeliverySnapshot> findDeliveriesByDepartment(final DepartmentId departmentId, final int offset,
                                                             final int limit) {
        return snapshots(this.deliveryRepository.findByDepartment(departmentId, offset, limit));
    }

    @Override
    public List<DeliverySnapshot> findDeliveriesBySupplier(final SupplierId supplierId, final int offset,
                                                           final int limit) {
        return snapshots(this.deliveryRepository.findBySupplier(supplierId, offset, limit));
    }

    @Override
    public DeliverySnapshot findDelivery(final DeliveryId deliveryId) {
        return find(deliveryId).snapshot();
    }

    @Override
    public DeliverySnapshot findDeliveryByShipmentId(final ShipmentId shipmentId) {
        return findShipmentDelivery(shipmentId).snapshot();
    }

    @Override
    public DeliverySnapshot findDeliveryByPalletId(final String palletId) {
        return findOutbound(DeliveryTarget.pallet(palletId))
                .map(Delivery::snapshot)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery for pallet " + palletId + " was not found"));
    }

    private SignatureId createSignature(final String recipientName, final String signature) {
        if (signature == null || signature.isBlank()) {
            throw new DeliveryModificationException("Recipient signature content is required");
        }
        final Signature recipientSignature = new Signature(recipientName, SignatureMethod.HANDWRITTEN, null,
                signature.getBytes(StandardCharsets.UTF_8));
        this.signatureRepository.save(recipientSignature);
        return recipientSignature.getSignatureId();
    }

    private DeliverySnapshot finish(final Delivery delivery, final SignatureId signatureId) {
        delivery.complete(delivery.getSupplierId(), this.operatorContextProvider.currentUserId(),
                this.operatorContextProvider.currentDepartmentId(), signatureId);
        if (delivery.getTarget().type() == DeliveryTargetType.SHIPMENT && delivery.getShipmentId() != null) {
            this.shipmentServicePort.markDelivered(delivery.getShipmentId());
        }
        return save(delivery, DeliveryCompleted::new);
    }

    private void cancel(final Delivery delivery) {
        delivery.markAsCanceled();
        save(delivery, DeliveryCanceled::new);
    }

    private void addShipmentRelations(final List<DeliveryRelation> relations, final DeliveryRelationType relation,
                                      final ShipmentId shipmentId) {
        this.deliveryRepository.findByTarget(DeliveryTarget.shipment(shipmentId))
                .forEach(related -> relations.add(new DeliveryRelation(relation, related.snapshot())));
    }

    private Delivery find(final DeliveryId deliveryId) {
        return this.deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery " + deliveryId.getId() + " was not found"));
    }

    private Optional<Delivery> findOutbound(final DeliveryTarget target) {
        return this.deliveryRepository.findByTargetAndType(target, DeliveryType.OUTBOUND);
    }

    private Optional<Delivery> findActiveShipmentDelivery(final ShipmentId shipmentId) {
        return this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND);
    }

    private Delivery findShipmentDelivery(final ShipmentId shipmentId) {
        return findOutbound(DeliveryTarget.shipment(shipmentId))
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery for shipment " + shipmentId.getValue()
                        + " was not found"));
    }

    private static List<DeliverySnapshot> snapshots(final List<Delivery> deliveries) {
        return deliveries.stream().map(Delivery::snapshot).toList();
    }

    private DeliverySnapshot saveModifiedWhen(final Delivery delivery, final boolean changed) {
        return changed ? save(delivery, DeliveryModified::new) : delivery.snapshot();
    }

    private DeliverySnapshot save(final Delivery delivery,
                                  final BiFunction<DeliverySnapshot, Instant, DeliveryChanged> event) {
        this.deliveryRepository.createOrUpdate(delivery);
        final DeliverySnapshot snapshot = delivery.snapshot();
        this.domainEventPublisher.publish(event.apply(snapshot, Instant.now()));
        return snapshot;
    }

    private void ensureDeliveryDoesNotExist(final DeliveryTarget target) {
        if (findOutbound(target).isPresent()) {
            throw new DeliveryAlreadyExistsException("Delivery for shipment " + target.id() + " already exists");
        }
    }
}
