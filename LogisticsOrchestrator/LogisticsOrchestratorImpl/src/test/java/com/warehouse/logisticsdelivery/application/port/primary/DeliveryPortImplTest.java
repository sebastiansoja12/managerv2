package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.logisticsdelivery.application.port.primary.command.AssignDeliverySupplierCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CommentDeliveryStepCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RegisterDeliveryAttemptCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CompleteDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDerivedDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreatePalletDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RecordDeliveryAttemptCommand;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryRelation;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.SourceShipment;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryDerivationType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.ORIGIN_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.USER_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.courierShipmentDelivery;
import static com.warehouse.logisticsdelivery.DeliveryFixture.shipmentDelivery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DeliveryPortImplTest {

    private DeliveryRepository deliveryRepository;
    private ShipmentServicePort shipmentServicePort;
    private DomainEventPublisher domainEventPublisher;
    private OperatorContextProvider operatorContextProvider;
    private DeliveryPortImpl deliveryPort;

    @BeforeEach
    void setUp() {
        this.deliveryRepository = mock(DeliveryRepository.class);
        this.shipmentServicePort = mock(ShipmentServicePort.class);
        this.domainEventPublisher = mock(DomainEventPublisher.class);
        this.operatorContextProvider = mock(OperatorContextProvider.class);
        this.deliveryPort = new DeliveryPortImpl(this.deliveryRepository, this.shipmentServicePort,
                this.domainEventPublisher, this.operatorContextProvider);
    }

    @Test
    void shouldCreateDeliveryAndPublishCreatedEvent() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());

        this.deliveryPort.createDelivery(new CreateDeliveryCommand(shipmentId, ORIGIN_DEPARTMENT_ID,
                DESTINATION_DEPARTMENT_ID, null, DeliveryMethod.COURIER, null, null, null, false, USER_ID));

        final Delivery saved = savedDelivery();
        assertThat(saved.getOriginDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(publishedEvent(DeliveryCreated.class).getSnapshot().deliveryId()).isEqualTo(saved.getDeliveryId());
    }

    @Test
    void shouldFindDeliveriesAssignedToSupplier() {
        final SupplierId supplierId = new SupplierId(7L);
        final Delivery delivery = courierShipmentDelivery(new ShipmentId(10L));
        when(this.deliveryRepository.findBySupplier(supplierId, 0, 50)).thenReturn(List.of(delivery));

        final List<DeliverySnapshot> deliveries = this.deliveryPort.findDeliveriesBySupplier(supplierId, 0, 50);

        assertThat(deliveries).extracting(DeliverySnapshot::deliveryId).containsExactly(delivery.getDeliveryId());
    }

    @Test
    void shouldThrowExceptionWhenDeliveryForShipmentAlreadyExists() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(shipmentDelivery(shipmentId)));

        assertThatThrownBy(() -> this.deliveryPort.createDelivery(new CreateDeliveryCommand(shipmentId, null, null,
                null, DeliveryMethod.COURIER, null, null, null, false, USER_ID)))
                .isInstanceOf(DeliveryAlreadyExistsException.class);
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldCreateDerivedDeliveryWithReversedRouteOfSourceDelivery() {
        final ShipmentId sourceShipmentId = new ShipmentId(10L);
        final ShipmentId shipmentId = new ShipmentId(11L);
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(sourceShipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(shipmentDelivery(sourceShipmentId)));
        when(this.shipmentServicePort.findSourceShipment(shipmentId))
                .thenReturn(Optional.of(new SourceShipment(sourceShipmentId, DeliveryDerivationType.RETURN)));

        this.deliveryPort.createDerivedDelivery(new CreateDerivedDeliveryCommand(shipmentId, false, USER_ID));

        final Delivery saved = savedDelivery();
        assertThat(saved.getOriginDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(saved.getDestinationDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(publishedEvent(DeliveryCreated.class).getSnapshot().shipmentId()).isEqualTo(shipmentId);
    }

    @Test
    void shouldCopyRouteOfSourceDeliveryForSplitShipment() {
        final ShipmentId sourceShipmentId = new ShipmentId(10L);
        final ShipmentId shipmentId = new ShipmentId(11L);
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(sourceShipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(shipmentDelivery(sourceShipmentId)));
        when(this.shipmentServicePort.findSourceShipment(shipmentId))
                .thenReturn(Optional.of(new SourceShipment(sourceShipmentId, DeliveryDerivationType.SPLIT)));

        this.deliveryPort.createDerivedDelivery(new CreateDerivedDeliveryCommand(shipmentId, false, USER_ID));

        final Delivery saved = savedDelivery();
        assertThat(saved.getOriginDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(saved.getDestinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
    }

    @Test
    void shouldThrowExceptionWhenSourceShipmentDoesNotExist() {
        when(this.deliveryRepository.findByTargetAndType(any(DeliveryTarget.class), any(DeliveryType.class)))
                .thenReturn(Optional.empty());
        when(this.shipmentServicePort.findSourceShipment(new ShipmentId(11L))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.deliveryPort.createDerivedDelivery(new CreateDerivedDeliveryCommand(
                new ShipmentId(11L), false, USER_ID))).isInstanceOf(SourceShipmentNotFoundException.class);
        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldThrowExceptionWhenSourceDeliveryDoesNotExist() {
        when(this.deliveryRepository.findByTargetAndType(any(DeliveryTarget.class), any(DeliveryType.class)))
                .thenReturn(Optional.empty());
        when(this.shipmentServicePort.findSourceShipment(new ShipmentId(11L)))
                .thenReturn(Optional.of(new SourceShipment(new ShipmentId(10L), DeliveryDerivationType.SPLIT)));

        assertThatThrownBy(() -> this.deliveryPort.createDerivedDelivery(new CreateDerivedDeliveryCommand(
                new ShipmentId(11L), false, USER_ID))).isInstanceOf(DeliveryNotFoundException.class);
        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldCreatePalletDeliveryAndCancelReplacedShipmentDeliveries() {
        final Delivery shipmentDelivery = courierShipmentDelivery(new ShipmentId(10L));
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(new ShipmentId(10L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(shipmentDelivery));
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(new ShipmentId(11L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.empty());

        this.deliveryPort.createPalletDelivery(new CreatePalletDeliveryCommand("pallet-1",
                List.of(new ShipmentId(10L), new ShipmentId(11L)), USER_ID));

        final ArgumentCaptor<Delivery> saved = ArgumentCaptor.forClass(Delivery.class);
        verify(this.deliveryRepository, times(2)).createOrUpdate(saved.capture());
        final Delivery palletDelivery = saved.getAllValues().get(0);
        assertThat(palletDelivery.getTarget().type()).isEqualTo(DeliveryTargetType.PALLET);
        assertThat(palletDelivery.getShipmentId()).isNull();
        assertThat(saved.getAllValues().get(1)).isSameAs(shipmentDelivery);
        assertThat(shipmentDelivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.CANCELED);
        assertThat(shipmentDelivery.getDeliverySteps()).hasSize(2);
        assertThat(publishedEvent(DeliveryCreated.class).getSnapshot().target().type())
                .isEqualTo(DeliveryTargetType.PALLET);
        assertThat(publishedEvent(DeliveryCanceled.class).getSnapshot().deliveryId())
                .isEqualTo(shipmentDelivery.getDeliveryId());
    }

    @Test
    void shouldNotDuplicatePalletDeliveryOnRepeatedEvent() {
        final Delivery existing = Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), USER_ID);
        final Delivery replaced = courierShipmentDelivery(new ShipmentId(10L));
        replaced.markAsCanceled();
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(existing));
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(new ShipmentId(10L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(replaced));

        this.deliveryPort.createPalletDelivery(new CreatePalletDeliveryCommand("pallet-1",
                List.of(new ShipmentId(10L)), USER_ID));

        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldCompleteActiveShipmentDeliveryAndPublishCompletedEvent() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = shipmentDelivery(shipmentId);
        when(this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(delivery));

        this.deliveryPort.completeDelivery(new CompleteDeliveryCommand(shipmentId, null, USER_ID, new DepartmentId(7L)));

        verify(this.deliveryRepository).createOrUpdate(delivery);
        assertThat(publishedEvent(DeliveryCompleted.class).getSnapshot().status())
                .isEqualTo(DeliveryLifecycleStatus.COMPLETED);
    }

    @Test
    void shouldRecordAttemptAndPublishAttemptRecordedEvent() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = shipmentDelivery(shipmentId);
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(delivery));

        final DeliverySnapshot snapshot = this.deliveryPort.recordAttempt(new RecordDeliveryAttemptCommand(
                DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND, DeliveryStatus.DELIVERY, USER_ID, null,
                new DepartmentId(7L), null, null, null));

        verify(this.deliveryRepository).createOrUpdate(delivery);
        assertThat(snapshot.deliverySteps()).hasSize(2);
        assertThat(publishedEvent(DeliveryAttemptRecorded.class).getSnapshot()).isEqualTo(snapshot);
    }

    @Test
    void shouldThrowExceptionWhenRecordingAttemptForMissingDelivery() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.RETURN))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.deliveryPort.recordAttempt(new RecordDeliveryAttemptCommand(
                DeliveryTarget.shipment(shipmentId), DeliveryType.RETURN, DeliveryStatus.RETURN, USER_ID, null,
                null, null, null, null))).isInstanceOf(DeliveryNotFoundException.class);
    }

    @Test
    void shouldChangeDestinationDepartmentAndPublishModifiedEvent() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = shipmentDelivery(shipmentId);
        when(this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(delivery));

        this.deliveryPort.changeDestinationDepartment(shipmentId, new DepartmentId(8L));

        verify(this.deliveryRepository).createOrUpdate(delivery);
        assertThat(publishedEvent(DeliveryModified.class).getSnapshot().destinationDepartmentId())
                .isEqualTo(new DepartmentId(8L));
    }

    @Test
    void shouldNotPublishEventWhenDestinationDepartmentDidNotChange() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(shipmentDelivery(shipmentId)));

        this.deliveryPort.changeDestinationDepartment(shipmentId, DESTINATION_DEPARTMENT_ID);

        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldIgnoreDestinationChangeWhenNoActiveShipmentDeliveryExists() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.empty());

        this.deliveryPort.changeDestinationDepartment(shipmentId, new DepartmentId(8L));

        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldCancelDeliveryAndPublishCanceledEvent() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));

        this.deliveryPort.cancelDelivery(delivery.getDeliveryId());

        verify(this.deliveryRepository).createOrUpdate(delivery);
        assertThat(publishedEvent(DeliveryCanceled.class).getSnapshot().status())
                .isEqualTo(DeliveryLifecycleStatus.CANCELED);
    }

    @Test
    void shouldThrowExceptionWhenDeliveryDoesNotExist() {
        final DeliveryId deliveryId = new DeliveryId("missing");
        when(this.deliveryRepository.findById(deliveryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.deliveryPort.findDelivery(deliveryId))
                .isInstanceOf(DeliveryNotFoundException.class);
    }

    private Delivery savedDelivery() {
        final ArgumentCaptor<Delivery> saved = ArgumentCaptor.forClass(Delivery.class);
        verify(this.deliveryRepository).createOrUpdate(saved.capture());
        return saved.getValue();
    }

    private <T extends DomainEvent> T publishedEvent(final Class<T> eventType) {
        final ArgumentCaptor<DomainEvent> events = ArgumentCaptor.forClass(DomainEvent.class);
        verify(this.domainEventPublisher, atLeastOnce()).publish(events.capture());
        return events.getAllValues().stream()
                .filter(eventType::isInstance)
                .map(eventType::cast)
                .findFirst()
                .orElseThrow();
    }

    @Test
    void shouldAssignSupplierAndPublishModifiedEvent() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));

        final DeliverySnapshot snapshot = this.deliveryPort.assignSupplier(new AssignDeliverySupplierCommand(
                delivery.getDeliveryId(), new SupplierId(4L), new VehicleId(9L)));

        verify(this.deliveryRepository).createOrUpdate(delivery);
        assertThat(snapshot.supplierId()).isEqualTo(new SupplierId(4L));
        assertThat(publishedEvent(DeliveryModified.class).getSnapshot().vehicleId()).isEqualTo(new VehicleId(9L));
    }

    @Test
    void shouldNotPublishEventWhenSupplierDidNotChange() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));

        this.deliveryPort.unassignSupplier(delivery.getDeliveryId());

        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldRegisterAttemptWithOperatorContextAndAssignedSupplier() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.assignSupplier(new SupplierId(4L), new VehicleId(9L));
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));
        when(this.operatorContextProvider.currentUserId()).thenReturn(USER_ID);
        when(this.operatorContextProvider.currentDepartmentId()).thenReturn(new DepartmentId(7L));

        final DeliverySnapshot snapshot = this.deliveryPort.registerAttempt(new RegisterDeliveryAttemptCommand(
                delivery.getDeliveryId(), DeliveryStatus.REJECTED, "Recipient absent"));

        assertThat(snapshot.deliverySteps().get(1).supplierId()).isEqualTo(new SupplierId(4L));
        assertThat(snapshot.deliverySteps().get(1).departmentId()).isEqualTo(new DepartmentId(7L));
        assertThat(publishedEvent(DeliveryAttemptRecorded.class).getSnapshot()).isEqualTo(snapshot);
    }

    @Test
    void shouldFinishDeliveryAndPublishCompletedEvent() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));
        when(this.operatorContextProvider.currentUserId()).thenReturn(USER_ID);
        when(this.operatorContextProvider.currentDepartmentId()).thenReturn(new DepartmentId(7L));

        final DeliverySnapshot snapshot = this.deliveryPort.finishDelivery(delivery.getDeliveryId());

        assertThat(snapshot.status()).isEqualTo(DeliveryLifecycleStatus.COMPLETED);
        assertThat(publishedEvent(DeliveryCompleted.class).getSnapshot()).isEqualTo(snapshot);
        verify(this.shipmentServicePort).markDelivered(new ShipmentId(10L));
    }

    @Test
    void shouldNotMarkShipmentDeliveredWhenFinishingPalletDelivery() {
        final Delivery delivery = Delivery.forPallet(DeliveryTarget.pallet("p-1"), USER_ID);
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));
        when(this.operatorContextProvider.currentUserId()).thenReturn(USER_ID);
        when(this.operatorContextProvider.currentDepartmentId()).thenReturn(new DepartmentId(7L));

        this.deliveryPort.finishDelivery(delivery.getDeliveryId());

        verify(this.shipmentServicePort, never()).markDelivered(any());
    }

    @Test
    void shouldCommentStepOfCompletedDeliveryAndPublishModifiedEvent() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.complete(null, USER_ID, null);
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));

        final DeliverySnapshot snapshot = this.deliveryPort.commentStep(new CommentDeliveryStepCommand(
                delivery.getDeliveryId(), 2, "Left at reception"));

        verify(this.deliveryRepository).createOrUpdate(delivery);
        assertThat(snapshot.deliverySteps().get(1).comment()).isEqualTo("Left at reception");
        assertThat(publishedEvent(DeliveryModified.class).getSnapshot()).isEqualTo(snapshot);
    }

    @Test
    void shouldFindSourceDerivedAndPalletRelatedDeliveries() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final ShipmentId sourceShipmentId = new ShipmentId(9L);
        final ShipmentId derivedShipmentId = new ShipmentId(11L);
        final Delivery delivery = shipmentDelivery(shipmentId);
        final Delivery source = shipmentDelivery(sourceShipmentId);
        final Delivery derived = shipmentDelivery(derivedShipmentId);
        final Delivery pallet = Delivery.forPallet(DeliveryTarget.pallet("p-1"), USER_ID);
        delivery.replaceByPalletDelivery(pallet.getDeliveryId(), USER_ID);
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));
        when(this.deliveryRepository.findById(pallet.getDeliveryId())).thenReturn(Optional.of(pallet));
        when(this.shipmentServicePort.findSourceShipment(shipmentId))
                .thenReturn(Optional.of(new SourceShipment(sourceShipmentId, DeliveryDerivationType.RETURN)));
        when(this.shipmentServicePort.findDerivedShipment(shipmentId)).thenReturn(Optional.of(derivedShipmentId));
        when(this.deliveryRepository.findByTarget(DeliveryTarget.shipment(sourceShipmentId))).thenReturn(List.of(source));
        when(this.deliveryRepository.findByTarget(DeliveryTarget.shipment(derivedShipmentId))).thenReturn(List.of(derived));

        final List<DeliveryRelation> relations = this.deliveryPort.findRelatedDeliveries(delivery.getDeliveryId());

        assertThat(relations).extracting(DeliveryRelation::relation).containsExactly(DeliveryRelationType.SOURCE,
                DeliveryRelationType.DERIVED, DeliveryRelationType.REPLACED_BY);
        assertThat(relations.get(0).delivery().shipmentId()).isEqualTo(sourceShipmentId);
        assertThat(relations.get(2).delivery().deliveryId()).isEqualTo(pallet.getDeliveryId());
    }

    @Test
    void shouldNotReportSourceShipmentAsDerived() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final ShipmentId sourceShipmentId = new ShipmentId(9L);
        final Delivery delivery = shipmentDelivery(shipmentId);
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));
        when(this.shipmentServicePort.findSourceShipment(shipmentId))
                .thenReturn(Optional.of(new SourceShipment(sourceShipmentId, DeliveryDerivationType.RETURN)));
        when(this.shipmentServicePort.findDerivedShipment(shipmentId)).thenReturn(Optional.of(sourceShipmentId));
        when(this.deliveryRepository.findByTarget(DeliveryTarget.shipment(sourceShipmentId)))
                .thenReturn(List.of(shipmentDelivery(sourceShipmentId)));

        final List<DeliveryRelation> relations = this.deliveryPort.findRelatedDeliveries(delivery.getDeliveryId());

        assertThat(relations).extracting(DeliveryRelation::relation).containsExactly(DeliveryRelationType.SOURCE);
    }
}
