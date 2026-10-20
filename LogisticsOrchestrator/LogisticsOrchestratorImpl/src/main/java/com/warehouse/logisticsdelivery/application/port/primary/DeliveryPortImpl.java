package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
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
import com.warehouse.logisticsdelivery.application.port.secondary.SourceShipment;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryRelationType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.event.DeliveryAttemptRecorded;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCanceled;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCompleted;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCreated;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryAlreadyExistsException;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryNotFoundException;
import com.warehouse.logisticsdelivery.domain.exception.SourceShipmentNotFoundException;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeliveryPortImpl implements DeliveryPort {

    private static final Logger log = LoggerFactory.getLogger(DeliveryPortImpl.class);

    private final DeliveryRepository deliveryRepository;

    private final ShipmentServicePort shipmentServicePort;

    private final DomainEventPublisher domainEventPublisher;

    private final OperatorContextProvider operatorContextProvider;

    public DeliveryPortImpl(final DeliveryRepository deliveryRepository,
                            final ShipmentServicePort shipmentServicePort,
                            final DomainEventPublisher domainEventPublisher,
                            final OperatorContextProvider operatorContextProvider) {
        this.deliveryRepository = deliveryRepository;
        this.shipmentServicePort = shipmentServicePort;
        this.domainEventPublisher = domainEventPublisher;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    @Transactional
    public void createDelivery(final CreateDeliveryCommand command) {
        final DeliveryTarget target = DeliveryTarget.shipment(command.shipmentId());
        ensureDeliveryDoesNotExist(target);

        final Delivery delivery = new Delivery(target, command.shipmentId(), DeliveryType.OUTBOUND,
                command.originDepartmentId(), command.destinationDepartmentId(), command.pickupMethod(),
                command.method(), command.pickupPointId(), command.deliveryPickupPointId(), command.signatureId(),
                command.signatureRequired(), command.userId());

        this.deliveryRepository.createOrUpdate(delivery);
        this.domainEventPublisher.publish(new DeliveryCreated(delivery.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void createDerivedDelivery(final CreateDerivedDeliveryCommand command) {
        ensureDeliveryDoesNotExist(DeliveryTarget.shipment(command.shipmentId()));

        final SourceShipment sourceShipment = this.shipmentServicePort.findSourceShipment(command.shipmentId())
                .orElseThrow(() -> new SourceShipmentNotFoundException("Source shipment for shipment "
                        + command.shipmentId().getValue() + " was not found"));
        final Delivery source = this.deliveryRepository.findByTargetAndType(
                        DeliveryTarget.shipment(sourceShipment.shipmentId()), DeliveryType.OUTBOUND)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery for source shipment "
                        + sourceShipment.shipmentId().getValue() + " was not found"));
        final Delivery delivery = Delivery.derivedFrom(source, command.shipmentId(), sourceShipment.derivationType(),
                command.signatureRequired(), command.userId());

        this.deliveryRepository.createOrUpdate(delivery);
        this.domainEventPublisher.publish(new DeliveryCreated(delivery.snapshot(), Instant.now()));
    }

    @Override
    @Transactional
    public void createPalletDelivery(final CreatePalletDeliveryCommand command) {
        final DeliveryTarget target = DeliveryTarget.pallet(command.palletId());
        final Delivery palletDelivery = this.deliveryRepository.findByTargetAndType(target, DeliveryType.OUTBOUND)
                .orElseGet(() -> {
                    final Delivery delivery = Delivery.forPallet(target, command.userId());
                    this.deliveryRepository.createOrUpdate(delivery);
                    this.domainEventPublisher.publish(new DeliveryCreated(delivery.snapshot(), Instant.now()));
                    log.info("Pallet delivery {} created for pallet {}", delivery.getDeliveryId().getId(),
                            command.palletId());
                    return delivery;
                });
        for (final ShipmentId shipmentId : command.shipmentIds()) {
            this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND)
                    .filter(delivery -> delivery.replaceByPalletDelivery(palletDelivery.getDeliveryId(),
                            command.userId()))
                    .ifPresent(delivery -> {
                        this.deliveryRepository.createOrUpdate(delivery);
                        this.domainEventPublisher.publish(new DeliveryCanceled(delivery.snapshot(), Instant.now()));
                    });
        }
    }

    @Override
    @Transactional
    public void completeDelivery(final CompleteDeliveryCommand command) {
        this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(command.shipmentId()),
                        DeliveryType.OUTBOUND)
                .ifPresent(delivery -> {
                    delivery.complete(command.supplierId(), command.userId(), command.departmentId());
                    this.deliveryRepository.createOrUpdate(delivery);
                    this.domainEventPublisher.publish(new DeliveryCompleted(delivery.snapshot(), Instant.now()));
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

        this.deliveryRepository.createOrUpdate(delivery);
        final DeliverySnapshot snapshot = delivery.snapshot();
        this.domainEventPublisher.publish(new DeliveryAttemptRecorded(snapshot, Instant.now()));
        return snapshot;
    }

    @Override
    @Transactional
    public void changeDestinationDepartment(final ShipmentId shipmentId, final DepartmentId destinationDepartmentId) {
        this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND)
                .filter(delivery -> delivery.changeDestinationDepartment(destinationDepartmentId))
                .ifPresent(this::saveModified);
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
        if (delivery.getReplacedByDeliveryId() != null) {
            this.deliveryRepository.findById(delivery.getReplacedByDeliveryId())
                    .ifPresent(replacing -> relations.add(new DeliveryRelation(DeliveryRelationType.REPLACED_BY,
                            replacing.snapshot())));
        }
        this.deliveryRepository.findReplacedBy(deliveryId)
                .forEach(replaced -> relations.add(new DeliveryRelation(DeliveryRelationType.REPLACES,
                        replaced.snapshot())));
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
        this.deliveryRepository.createOrUpdate(delivery);
        final DeliverySnapshot snapshot = delivery.snapshot();
        this.domainEventPublisher.publish(new DeliveryAttemptRecorded(snapshot, Instant.now()));
        return snapshot;
    }

    @Override
    @Transactional
    public DeliverySnapshot finishDelivery(final DeliveryId deliveryId) {
        final Delivery delivery = find(deliveryId);
        delivery.complete(delivery.getSupplierId(), this.operatorContextProvider.currentUserId(),
                this.operatorContextProvider.currentDepartmentId());
        this.deliveryRepository.createOrUpdate(delivery);
        if (delivery.getTarget().type() == DeliveryTargetType.SHIPMENT && delivery.getShipmentId() != null) {
            this.shipmentServicePort.markDelivered(delivery.getShipmentId());
        }
        final DeliverySnapshot snapshot = delivery.snapshot();
        this.domainEventPublisher.publish(new DeliveryCompleted(snapshot, Instant.now()));
        return snapshot;
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
        delivery.markAsCanceled();
        this.deliveryRepository.createOrUpdate(delivery);
        this.domainEventPublisher.publish(new DeliveryCanceled(delivery.snapshot(), Instant.now()));
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
    public DeliverySnapshot findDelivery(final DeliveryId deliveryId) {
        return find(deliveryId).snapshot();
    }

    @Override
    public DeliverySnapshot findDeliveryByShipmentId(final ShipmentId shipmentId) {
        return this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND)
                .map(Delivery::snapshot)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery for shipment " + shipmentId.getValue()
                        + " was not found"));
    }

    @Override
    public DeliverySnapshot findDeliveryByPalletId(final String palletId) {
        return this.deliveryRepository.findByTargetAndType(DeliveryTarget.pallet(palletId), DeliveryType.OUTBOUND)
                .map(Delivery::snapshot)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery for pallet " + palletId + " was not found"));
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

    private DeliverySnapshot saveModifiedWhen(final Delivery delivery, final boolean changed) {
        if (changed) {
            saveModified(delivery);
        }
        return delivery.snapshot();
    }

    private void saveModified(final Delivery delivery) {
        this.deliveryRepository.createOrUpdate(delivery);
        this.domainEventPublisher.publish(new DeliveryModified(delivery.snapshot(), Instant.now()));
    }

    private void ensureDeliveryDoesNotExist(final DeliveryTarget target) {
        if (this.deliveryRepository.findByTargetAndType(target, DeliveryType.OUTBOUND).isPresent()) {
            throw new DeliveryAlreadyExistsException("Delivery for shipment " + target.id() + " already exists");
        }
    }
}
