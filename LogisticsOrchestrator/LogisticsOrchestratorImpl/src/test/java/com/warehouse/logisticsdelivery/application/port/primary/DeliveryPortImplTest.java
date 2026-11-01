package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.logisticsdelivery.application.service.DestinationCapacityAllocator;
import com.warehouse.logisticsdelivery.domain.service.DepartmentCapacityBalancer;
import java.time.Clock;
import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PalletId;
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
import com.warehouse.logisticsdelivery.domain.model.DeliveryStep;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryCommentCode;
import com.warehouse.logisticsdelivery.application.port.primary.command.RevertPalletDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.UnloadPalletDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RecordDeliveryAttemptCommand;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryRelation;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.DestinationDepartmentResolverPort;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.SignatureRepository;
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
import com.warehouse.logisticsdelivery.domain.exception.DeliveryModificationException;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryNotFoundException;
import com.warehouse.logisticsdelivery.domain.exception.DestinationDepartmentNotResolvedException;
import com.warehouse.logisticsdelivery.domain.exception.SourceShipmentNotFoundException;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.model.Signature;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;
import com.warehouse.logisticsdelivery.domain.vo.RecipientAddress;
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
    private SignatureRepository signatureRepository;
    private DestinationDepartmentResolverPort destinationDepartmentResolverPort;
    private DeliveryPortImpl deliveryPort;

    @BeforeEach
    void setUp() {
        this.deliveryRepository = mock(DeliveryRepository.class);
        this.shipmentServicePort = mock(ShipmentServicePort.class);
        this.domainEventPublisher = mock(DomainEventPublisher.class);
        this.operatorContextProvider = mock(OperatorContextProvider.class);
        this.signatureRepository = mock(SignatureRepository.class);
        this.destinationDepartmentResolverPort = mock(DestinationDepartmentResolverPort.class);
        this.deliveryPort = new DeliveryPortImpl(this.deliveryRepository, this.shipmentServicePort,
                this.domainEventPublisher, this.operatorContextProvider, this.signatureRepository,
                this.destinationDepartmentResolverPort, new DestinationCapacityAllocator(this.deliveryRepository,
                        List::of, new DepartmentCapacityBalancer(), Clock.systemDefaultZone()));
    }

    @Test
    void shouldCreateDeliveryAndPublishCreatedEvent() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());

        this.deliveryPort.createDelivery(new CreateDeliveryCommand(shipmentId, ORIGIN_DEPARTMENT_ID,
                DESTINATION_DEPARTMENT_ID, null, DeliveryMethod.COURIER, null, null, null, false, USER_ID));

        final Delivery saved = savedDelivery();
        assertThat(saved.getOriginDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(publishedEvent(DeliveryCreated.class).getSnapshot().deliveryId()).isEqualTo(saved.getDeliveryId());
    }

    @Test
    void shouldResolveMissingDestinationOfCourierDeliveryFromRecipientAddress() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());
        when(this.shipmentServicePort.findRecipientAddress(shipmentId)).thenReturn(Optional.of(RECIPIENT_ADDRESS));
        when(this.destinationDepartmentResolverPort.resolve(RECIPIENT_ADDRESS))
                .thenReturn(Optional.of(DESTINATION_DEPARTMENT_ID));

        this.deliveryPort.createDelivery(new CreateDeliveryCommand(shipmentId, ORIGIN_DEPARTMENT_ID, null, null,
                DeliveryMethod.COURIER, null, null, null, false, USER_ID));

        assertThat(savedDelivery().getDestinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
    }

    @Test
    void shouldCreateDeliveryWithoutDestinationWhenItCannotBeResolved() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());
        when(this.shipmentServicePort.findRecipientAddress(shipmentId)).thenReturn(Optional.of(RECIPIENT_ADDRESS));
        when(this.destinationDepartmentResolverPort.resolve(RECIPIENT_ADDRESS)).thenReturn(Optional.empty());

        this.deliveryPort.createDelivery(new CreateDeliveryCommand(shipmentId, ORIGIN_DEPARTMENT_ID, null, null,
                DeliveryMethod.COURIER, null, null, null, false, USER_ID));

        assertThat(savedDelivery().getDestinationDepartmentId()).isNull();
        assertThat(publishedEvent(DeliveryCreated.class)).isNotNull();
    }

    @Test
    void shouldNotResolveDestinationOfPickupPointDelivery() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());

        this.deliveryPort.createDelivery(new CreateDeliveryCommand(shipmentId, ORIGIN_DEPARTMENT_ID, null, null,
                DeliveryMethod.LOCKER, null, null, null, false, USER_ID));

        verifyNoInteractions(this.destinationDepartmentResolverPort);
    }

    @Test
    void shouldResolveDestinationOfExistingDeliveryAndPublishModification() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = courierShipmentDelivery(shipmentId);
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));
        when(this.shipmentServicePort.findRecipientAddress(shipmentId)).thenReturn(Optional.of(RECIPIENT_ADDRESS));
        when(this.destinationDepartmentResolverPort.resolve(RECIPIENT_ADDRESS))
                .thenReturn(Optional.of(DESTINATION_DEPARTMENT_ID));

        this.deliveryPort.resolveDestinationDepartment(delivery.getDeliveryId());

        assertThat(delivery.getDestinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(delivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.MODIFIED);
        assertThat(publishedEvent(DeliveryModified.class).getSnapshot().deliveryId()).isEqualTo(delivery.getDeliveryId());
    }

    @Test
    void shouldRejectResolvingDestinationWhenItCannotBeDetermined() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = courierShipmentDelivery(shipmentId);
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));
        when(this.shipmentServicePort.findRecipientAddress(shipmentId)).thenReturn(Optional.of(RECIPIENT_ADDRESS));
        when(this.destinationDepartmentResolverPort.resolve(RECIPIENT_ADDRESS)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.deliveryPort.resolveDestinationDepartment(delivery.getDeliveryId()))
                .isInstanceOf(DestinationDepartmentNotResolvedException.class);
        verifyNoInteractions(this.domainEventPublisher);
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
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(shipmentDelivery(shipmentId)));

        assertThatThrownBy(() -> this.deliveryPort.createDelivery(new CreateDeliveryCommand(shipmentId, null, null,
                null, DeliveryMethod.COURIER, null, null, null, false, USER_ID)))
                .isInstanceOf(DeliveryAlreadyExistsException.class);
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldCreateDeliveryWhenOnlyCanceledDeliveryExistsForShipment() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery canceled = shipmentDelivery(shipmentId);
        canceled.markAsCanceled();
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(canceled));

        this.deliveryPort.createDelivery(new CreateDeliveryCommand(shipmentId, ORIGIN_DEPARTMENT_ID,
                DESTINATION_DEPARTMENT_ID, null, DeliveryMethod.COURIER, null, null, null, false, USER_ID));

        final Delivery saved = savedDelivery();
        assertThat(saved.getDeliveryId()).isNotEqualTo(canceled.getDeliveryId());
        assertThat(saved.isActive()).isTrue();
        assertThat(publishedEvent(DeliveryCreated.class).getSnapshot().deliveryId()).isEqualTo(saved.getDeliveryId());
    }

    @Test
    void shouldCreateDerivedDeliveryWithReversedRouteOfSourceDelivery() {
        final ShipmentId sourceShipmentId = new ShipmentId(10L);
        final ShipmentId shipmentId = new ShipmentId(11L);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(sourceShipmentId),
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
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(sourceShipmentId),
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
        when(this.deliveryRepository.findCurrentByTargetAndType(any(DeliveryTarget.class), any(DeliveryType.class)))
                .thenReturn(Optional.empty());
        when(this.shipmentServicePort.findSourceShipment(new ShipmentId(11L))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.deliveryPort.createDerivedDelivery(new CreateDerivedDeliveryCommand(
                new ShipmentId(11L), false, USER_ID))).isInstanceOf(SourceShipmentNotFoundException.class);
        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldThrowExceptionWhenSourceDeliveryDoesNotExist() {
        when(this.deliveryRepository.findCurrentByTargetAndType(any(DeliveryTarget.class), any(DeliveryType.class)))
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
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(new ShipmentId(10L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(shipmentDelivery));
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(new ShipmentId(11L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.empty());

        this.deliveryPort.createPalletDelivery(new CreatePalletDeliveryCommand("pallet-1",
                List.of(new ShipmentId(10L), new ShipmentId(11L)), ORIGIN_DEPARTMENT_ID, DESTINATION_DEPARTMENT_ID,
                USER_ID));

        final ArgumentCaptor<Delivery> saved = ArgumentCaptor.forClass(Delivery.class);
        verify(this.deliveryRepository, times(2)).createOrUpdate(saved.capture());
        final Delivery palletDelivery = saved.getAllValues().get(0);
        assertThat(palletDelivery.getTarget().type()).isEqualTo(DeliveryTargetType.PALLET);
        assertThat(palletDelivery.getShipmentId()).isNull();
        assertThat(palletDelivery.getOriginDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(palletDelivery.getDestinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
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
        final Delivery existing = Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), null, null, USER_ID);
        final Delivery replaced = courierShipmentDelivery(new ShipmentId(10L));
        replaced.markAsCanceled();
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(existing));
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(new ShipmentId(10L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(replaced));

        this.deliveryPort.createPalletDelivery(new CreatePalletDeliveryCommand("pallet-1",
                List.of(new ShipmentId(10L)), ORIGIN_DEPARTMENT_ID, DESTINATION_DEPARTMENT_ID, USER_ID));

        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldReopenCanceledPalletDeliveryWhenPalletIsLoadedAgain() {
        final Delivery existing = Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), null, null, USER_ID);
        existing.markAsCanceled();
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(existing));
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(new ShipmentId(10L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.empty());

        this.deliveryPort.createPalletDelivery(new CreatePalletDeliveryCommand("pallet-1",
                List.of(new ShipmentId(10L)), ORIGIN_DEPARTMENT_ID, DESTINATION_DEPARTMENT_ID, USER_ID));

        verify(this.deliveryRepository).createOrUpdate(existing);
        assertThat(existing.isActive()).isTrue();
        assertThat(existing.getOriginDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(existing.getDestinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(publishedEvent(DeliveryModified.class).getSnapshot().deliveryId())
                .isEqualTo(existing.getDeliveryId());
    }

    @Test
    void shouldCancelPalletDeliveryAndContinueReplacedShipmentDeliveriesWhenLoadingIsReverted() {
        final Delivery palletDelivery = Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), null, null, USER_ID);
        final Delivery shipmentDelivery = shipmentDelivery(new ShipmentId(10L));
        shipmentDelivery.replaceByPalletDelivery(palletDelivery.getDeliveryId(), USER_ID);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(palletDelivery));
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(new ShipmentId(10L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(shipmentDelivery));
        when(this.deliveryRepository.findReplacedBy(palletDelivery.getDeliveryId()))
                .thenReturn(List.of(shipmentDelivery));

        this.deliveryPort.revertPalletDelivery(new RevertPalletDeliveryCommand("pallet-1",
                List.of(new ShipmentId(10L)), USER_ID));

        final ArgumentCaptor<Delivery> saved = ArgumentCaptor.forClass(Delivery.class);
        verify(this.deliveryRepository, times(2)).createOrUpdate(saved.capture());
        final Delivery continuation = saved.getAllValues().get(1);
        assertThat(palletDelivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.CANCELED);
        assertThat(shipmentDelivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.CANCELED);
        assertThat(continuation.getDeliveryId()).isNotEqualTo(shipmentDelivery.getDeliveryId());
        assertThat(continuation.getShipmentId()).isEqualTo(new ShipmentId(10L));
        assertThat(continuation.getOriginDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(continuation.getDeliverySteps().getLast().commentCode())
                .isEqualTo(DeliveryCommentCode.PALLET_LOADING_REVERTED);
        assertThat(publishedEvent(DeliveryCanceled.class).getSnapshot().deliveryId())
                .isEqualTo(palletDelivery.getDeliveryId());
        assertThat(publishedEvent(DeliveryCreated.class).getSnapshot().deliveryId())
                .isEqualTo(continuation.getDeliveryId());
    }

    @Test
    void shouldCompletePalletDeliveryAndContinueShipmentDeliveriesFromUnloadDepartment() {
        final DepartmentId unloadDepartmentId = new DepartmentId(8L);
        final Delivery palletDelivery = Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), ORIGIN_DEPARTMENT_ID,
                unloadDepartmentId, USER_ID);
        final Delivery shipmentDelivery = shipmentDelivery(new ShipmentId(10L));
        shipmentDelivery.replaceByPalletDelivery(palletDelivery.getDeliveryId(), USER_ID);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(palletDelivery));
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(new ShipmentId(10L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(shipmentDelivery));
        when(this.deliveryRepository.findReplacedBy(palletDelivery.getDeliveryId()))
                .thenReturn(List.of(shipmentDelivery));

        this.deliveryPort.unloadPalletDelivery(new UnloadPalletDeliveryCommand(new PalletId("pallet-1"),
                List.of(new ShipmentId(10L)), unloadDepartmentId, USER_ID));

        final ArgumentCaptor<Delivery> saved = ArgumentCaptor.forClass(Delivery.class);
        verify(this.deliveryRepository, times(2)).createOrUpdate(saved.capture());
        final Delivery continuation = saved.getAllValues().get(1);
        assertThat(palletDelivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.COMPLETED);
        assertThat(continuation.isActive()).isTrue();
        assertThat(continuation.getOriginDepartmentId()).isEqualTo(unloadDepartmentId);
        assertThat(continuation.getDestinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(continuation.getDeliverySteps().getLast().commentCode())
                .isEqualTo(DeliveryCommentCode.UNLOADED_FROM_PALLET);
        assertThat(publishedEvent(DeliveryCompleted.class).getSnapshot().deliveryId())
                .isEqualTo(palletDelivery.getDeliveryId());
        assertThat(publishedEvent(DeliveryCreated.class).getSnapshot().deliveryId())
                .isEqualTo(continuation.getDeliveryId());
    }

    @Test
    void shouldNotContinueShipmentDeliveryWhenCurrentDeliveryAlreadyExists() {
        final Delivery palletDelivery = Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), null, null, USER_ID);
        final Delivery replaced = shipmentDelivery(new ShipmentId(10L));
        replaced.replaceByPalletDelivery(palletDelivery.getDeliveryId(), USER_ID);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(palletDelivery));
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(new ShipmentId(10L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(shipmentDelivery(new ShipmentId(10L))));
        when(this.deliveryRepository.findReplacedBy(palletDelivery.getDeliveryId())).thenReturn(List.of(replaced));

        this.deliveryPort.unloadPalletDelivery(new UnloadPalletDeliveryCommand(new PalletId("pallet-1"),
                List.of(new ShipmentId(10L)), DESTINATION_DEPARTMENT_ID, USER_ID));

        verify(this.deliveryRepository).createOrUpdate(palletDelivery);
        verify(this.deliveryRepository, times(1)).createOrUpdate(any());
    }

    @Test
    void shouldNotContinueDeliveryOfCanceledShipmentAfterPalletUnloading() {
        final Delivery palletDelivery = Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), null, null, USER_ID);
        final Delivery replaced = shipmentDelivery(new ShipmentId(10L));
        replaced.replaceByPalletDelivery(palletDelivery.getDeliveryId(), USER_ID);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(palletDelivery));
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(new ShipmentId(10L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(replaced));
        when(this.deliveryRepository.findReplacedBy(palletDelivery.getDeliveryId())).thenReturn(List.of(replaced));
        when(this.shipmentServicePort.isCanceled(new ShipmentId(10L))).thenReturn(true);

        this.deliveryPort.unloadPalletDelivery(new UnloadPalletDeliveryCommand(new PalletId("pallet-1"),
                List.of(new ShipmentId(10L)), DESTINATION_DEPARTMENT_ID, USER_ID));

        verify(this.deliveryRepository, times(1)).createOrUpdate(any());
    }

    @Test
    void shouldIgnoreUnloadingWhenPalletDeliveryIsAlreadyCompleted() {
        final Delivery palletDelivery = Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), null, null, USER_ID);
        palletDelivery.complete(null, USER_ID, DESTINATION_DEPARTMENT_ID);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(palletDelivery));

        this.deliveryPort.unloadPalletDelivery(new UnloadPalletDeliveryCommand(new PalletId("pallet-1"),
                List.of(new ShipmentId(10L)), DESTINATION_DEPARTMENT_ID, USER_ID));

        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldIgnoreRevertWhenPalletDeliveryIsAlreadyCanceled() {
        final Delivery palletDelivery = Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), null, null, USER_ID);
        palletDelivery.markAsCanceled();
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(palletDelivery));

        this.deliveryPort.revertPalletDelivery(new RevertPalletDeliveryCommand("pallet-1",
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
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
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
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.RETURN))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.deliveryPort.recordAttempt(new RecordDeliveryAttemptCommand(
                DeliveryTarget.shipment(shipmentId), DeliveryType.RETURN, DeliveryStatus.RETURN, USER_ID, null,
                null, null, null, null))).isInstanceOf(DeliveryNotFoundException.class);
    }

    @Test
    void shouldRecalculateDestinationWhenRecipientAddressChanges() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = courierShipmentDelivery(shipmentId);
        when(this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(delivery));
        when(this.destinationDepartmentResolverPort.resolve(RECIPIENT_ADDRESS))
                .thenReturn(Optional.of(new DepartmentId(8L)));

        this.deliveryPort.changeRecipientAddress(shipmentId, RECIPIENT_ADDRESS);

        verify(this.deliveryRepository).createOrUpdate(delivery);
        assertThat(publishedEvent(DeliveryModified.class).getSnapshot().destinationDepartmentId())
                .isEqualTo(new DepartmentId(8L));
    }

    @Test
    void shouldNotPublishEventWhenRecalculatedDestinationDidNotChange() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = courierShipmentDelivery(shipmentId);
        delivery.assignDestinationDepartment(DESTINATION_DEPARTMENT_ID);
        when(this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(delivery));
        when(this.destinationDepartmentResolverPort.resolve(RECIPIENT_ADDRESS))
                .thenReturn(Optional.of(DESTINATION_DEPARTMENT_ID));

        this.deliveryPort.changeRecipientAddress(shipmentId, RECIPIENT_ADDRESS);

        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldNotRecalculateDestinationOfPickupPointDelivery() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(shipmentDelivery(shipmentId)));

        this.deliveryPort.changeRecipientAddress(shipmentId, RECIPIENT_ADDRESS);

        verifyNoInteractions(this.destinationDepartmentResolverPort, this.domainEventPublisher);
        verify(this.deliveryRepository, never()).createOrUpdate(any());
    }

    @Test
    void shouldIgnoreRecipientAddressChangeWhenNoActiveShipmentDeliveryExists() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.empty());

        this.deliveryPort.changeRecipientAddress(shipmentId, RECIPIENT_ADDRESS);

        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.destinationDepartmentResolverPort, this.domainEventPublisher);
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
    void shouldNotPublishCanceledEventWhenDeliveryIsAlreadyCanceled() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.markAsCanceled();
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));

        this.deliveryPort.cancelDelivery(delivery.getDeliveryId());

        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldThrowExceptionWhenRecipientSignedWithoutSignatureContent() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(shipmentDelivery(shipmentId)));

        assertThatThrownBy(() -> this.deliveryPort.completeShipmentDelivery(shipmentId, true, "Jan Kowalski", null))
                .isInstanceOf(DeliveryModificationException.class);
        verifyNoInteractions(this.signatureRepository, this.domainEventPublisher);
    }

    @Test
    void shouldCancelActiveShipmentDeliveriesAndPublishCanceledEvents() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = shipmentDelivery(shipmentId);
        when(this.deliveryRepository.findByTarget(DeliveryTarget.shipment(shipmentId))).thenReturn(List.of(delivery));

        this.deliveryPort.cancelShipmentDeliveries(shipmentId);

        verify(this.deliveryRepository).createOrUpdate(delivery);
        assertThat(delivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.CANCELED);
        assertThat(publishedEvent(DeliveryCanceled.class).getSnapshot().deliveryId())
                .isEqualTo(delivery.getDeliveryId());
    }

    @Test
    void shouldIgnoreCanceledAndCompletedDeliveriesWhenCancelingShipmentDeliveries() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery canceled = shipmentDelivery(shipmentId);
        canceled.markAsCanceled();
        final Delivery completed = shipmentDelivery(shipmentId);
        completed.complete(null, USER_ID, new DepartmentId(7L));
        when(this.deliveryRepository.findByTarget(DeliveryTarget.shipment(shipmentId)))
                .thenReturn(List.of(canceled, completed));

        this.deliveryPort.cancelShipmentDeliveries(shipmentId);

        assertThat(completed.getStatus()).isEqualTo(DeliveryLifecycleStatus.COMPLETED);
        verify(this.deliveryRepository, never()).createOrUpdate(any());
        verifyNoInteractions(this.domainEventPublisher);
    }

    @Test
    void shouldThrowExceptionWhenDeliveryDoesNotExist() {
        final DeliveryId deliveryId = new DeliveryId("missing");
        when(this.deliveryRepository.findById(deliveryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.deliveryPort.findDelivery(deliveryId))
                .isInstanceOf(DeliveryNotFoundException.class);
    }

    private static final RecipientAddress RECIPIENT_ADDRESS =
            new RecipientAddress("Warszawa", "Długa 1", "00-001", "PL");

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
    void shouldAssignScanningSupplierWithVehicleToShipmentDelivery() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = shipmentDelivery(shipmentId);
        delivery.assignSupplier(new SupplierId(3L), new VehicleId(8L));
        when(this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(delivery));

        this.deliveryPort.assignShipmentSupplier(shipmentId, new SupplierId(4L), new VehicleId(9L));

        verify(this.deliveryRepository).createOrUpdate(delivery);
        final DeliverySnapshot snapshot = publishedEvent(DeliveryModified.class).getSnapshot();
        assertThat(snapshot.supplierId()).isEqualTo(new SupplierId(4L));
        assertThat(snapshot.vehicleId()).isEqualTo(new VehicleId(9L));
    }

    @Test
    void shouldNotSaveWhenScanningSupplierAndVehicleAreAlreadyAssigned() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = shipmentDelivery(shipmentId);
        delivery.assignSupplier(new SupplierId(4L), new VehicleId(9L));
        when(this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(delivery));

        this.deliveryPort.assignShipmentSupplier(shipmentId, new SupplierId(4L), new VehicleId(9L));

        verify(this.deliveryRepository, never()).createOrUpdate(any());
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
    void shouldAttachRecipientSignatureToCompletedStep() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = shipmentDelivery(shipmentId);
        when(this.deliveryRepository.findCurrentByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(delivery));
        when(this.deliveryRepository.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));
        final ArgumentCaptor<Signature> signature = ArgumentCaptor.forClass(Signature.class);

        this.deliveryPort.completeShipmentDelivery(shipmentId, true, "Jan Kowalski", "<svg/>");

        verify(this.signatureRepository).save(signature.capture());
        when(this.signatureRepository.findById(signature.getValue().getSignatureId()))
                .thenReturn(Optional.of(signature.getValue()));
        assertThat(signature.getValue().getSignerName()).isEqualTo("Jan Kowalski");
        assertThat(this.deliveryPort.findSignature(delivery.getDeliveryId())).contains(signature.getValue());
    }

    @Test
    void shouldNotMarkShipmentDeliveredWhenFinishingPalletDelivery() {
        final Delivery delivery = Delivery.forPallet(DeliveryTarget.pallet("p-1"), null, null, USER_ID);
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
        final Delivery pallet = Delivery.forPallet(DeliveryTarget.pallet("p-1"), null, null, USER_ID);
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
    void shouldFindContinuedAndContinuingDeliveriesOfSameShipment() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery pallet = Delivery.forPallet(DeliveryTarget.pallet("p-1"), null, null, USER_ID);
        final Delivery replaced = shipmentDelivery(shipmentId);
        replaced.replaceByPalletDelivery(pallet.getDeliveryId(), USER_ID);
        final Delivery continuation = Delivery.continuedAfterPalletUnloading(replaced, DESTINATION_DEPARTMENT_ID,
                USER_ID);
        when(this.deliveryRepository.findById(continuation.getDeliveryId())).thenReturn(Optional.of(continuation));
        when(this.deliveryRepository.findById(replaced.getDeliveryId())).thenReturn(Optional.of(replaced));
        when(this.deliveryRepository.findByTarget(DeliveryTarget.shipment(shipmentId)))
                .thenReturn(List.of(continuation, replaced));

        final List<DeliveryRelation> continuationRelations =
                this.deliveryPort.findRelatedDeliveries(continuation.getDeliveryId());
        final List<DeliveryRelation> replacedRelations =
                this.deliveryPort.findRelatedDeliveries(replaced.getDeliveryId());

        assertThat(continuationRelations).extracting(DeliveryRelation::relation)
                .containsExactly(DeliveryRelationType.CONTINUES);
        assertThat(continuationRelations.getFirst().delivery().deliveryId()).isEqualTo(replaced.getDeliveryId());
        assertThat(replacedRelations).extracting(DeliveryRelation::relation)
                .contains(DeliveryRelationType.CONTINUED_BY);
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
