package com.warehouse.logistics.domain;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.logistics.domain.enumeration.DeliveryDerivationType;
import com.warehouse.logistics.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.DeliveryType;
import com.warehouse.logistics.domain.enumeration.PickupMethod;
import com.warehouse.logistics.domain.model.CreateDerivedDeliveryCommand;
import com.warehouse.logistics.domain.model.Delivery;
import com.warehouse.logistics.domain.model.DeliveryTarget;
import com.warehouse.logistics.domain.port.primary.LogisticsPortImpl;
import com.warehouse.logistics.domain.service.LogisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeliveryRouteTest {

    private static final UserId USER_ID = new UserId(1L);
    private static final DepartmentId ORIGIN_DEPARTMENT_ID = new DepartmentId(3L);
    private static final DepartmentId DESTINATION_DEPARTMENT_ID = new DepartmentId(5L);
    private static final PickupPointId PICKUP_POINT_ID = new PickupPointId(UUID.randomUUID());
    private static final PickupPointId DELIVERY_PICKUP_POINT_ID = new PickupPointId(UUID.randomUUID());

    private LogisticsService logisticsService;
    private LogisticsPortImpl logisticsPort;

    @BeforeEach
    void setUp() {
        this.logisticsService = mock(LogisticsService.class);
        this.logisticsPort = new LogisticsPortImpl(this.logisticsService);
    }

    @Test
    void shouldKeepRouteOnDeliveryAndNotOnCreatedStep() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));

        assertThat(delivery.getOriginDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(delivery.getDestinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(delivery.getPickupMethod()).isEqualTo(PickupMethod.PICKUP_POINT);
        assertThat(delivery.getMethod()).isEqualTo(DeliveryMethod.LOCKER);
        assertThat(delivery.getPickupPointId()).isEqualTo(PICKUP_POINT_ID);
        assertThat(delivery.getDeliveryPickupPointId()).isEqualTo(DELIVERY_PICKUP_POINT_ID);
        assertThat(delivery.getDeliverySteps()).hasSize(1);
        assertThat(delivery.getDeliverySteps().get(0).departmentId()).isNull();
    }

    @Test
    void shouldChangeDestinationDepartmentWhenDeliveryIsActive() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        final DepartmentId newDestinationDepartmentId = new DepartmentId(8L);

        delivery.changeDestinationDepartment(newDestinationDepartmentId);

        assertThat(delivery.getDestinationDepartmentId()).isEqualTo(newDestinationDepartmentId);
        assertThat(delivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.MODIFIED);
    }

    @Test
    void shouldThrowExceptionWhenChangingDestinationDepartmentOfCompletedDelivery() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        delivery.markAsCompleted();

        assertThatThrownBy(() -> delivery.changeDestinationDepartment(new DepartmentId(8L)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldChangeDestinationDepartmentOfActiveShipmentDelivery() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        final Delivery delivery = shipmentDelivery(shipmentId);
        when(this.logisticsService.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(delivery));

        this.logisticsPort.changeDestinationDepartment(shipmentId, new DepartmentId(8L));

        verify(this.logisticsService).createOrUpdate(delivery);
        assertThat(delivery.getDestinationDepartmentId()).isEqualTo(new DepartmentId(8L));
    }

    @Test
    void shouldIgnoreDestinationChangeWhenNoActiveShipmentDeliveryExists() {
        final ShipmentId shipmentId = new ShipmentId(10L);
        when(this.logisticsService.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());

        this.logisticsPort.changeDestinationDepartment(shipmentId, new DepartmentId(8L));

        verify(this.logisticsService, never()).createOrUpdate(any());
    }

    @Test
    void shouldCreateDeliveryWithRouteCopiedFromSourceDelivery() {
        final ShipmentId sourceShipmentId = new ShipmentId(10L);
        final ShipmentId shipmentId = new ShipmentId(11L);
        mockDeliveries(sourceShipmentId, shipmentId);

        this.logisticsPort.createDerivedDelivery(derivedCommand(shipmentId, sourceShipmentId,
                DeliveryDerivationType.REDIRECT));

        final Delivery created = capturedDelivery();
        assertThat(created.getOriginDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(created.getDestinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(created.getPickupMethod()).isEqualTo(PickupMethod.PICKUP_POINT);
        assertThat(created.getMethod()).isEqualTo(DeliveryMethod.LOCKER);
        assertThat(created.getPickupPointId()).isEqualTo(PICKUP_POINT_ID);
        assertThat(created.getDeliveryPickupPointId()).isEqualTo(DELIVERY_PICKUP_POINT_ID);
    }

    @Test
    void shouldCreateDeliveryWithReversedRouteOfSourceDelivery() {
        final ShipmentId sourceShipmentId = new ShipmentId(10L);
        final ShipmentId shipmentId = new ShipmentId(11L);
        mockDeliveries(sourceShipmentId, shipmentId);

        this.logisticsPort.createDerivedDelivery(derivedCommand(shipmentId, sourceShipmentId,
                DeliveryDerivationType.RETURN));

        final Delivery created = capturedDelivery();
        assertThat(created.getOriginDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(created.getDestinationDepartmentId()).isEqualTo(ORIGIN_DEPARTMENT_ID);
        assertThat(created.getPickupMethod()).isEqualTo(PickupMethod.DEPARTMENT);
        assertThat(created.getMethod()).isEqualTo(DeliveryMethod.COURIER);
        assertThat(created.getPickupPointId()).isNull();
        assertThat(created.getDeliveryPickupPointId()).isNull();
    }

    private void mockDeliveries(final ShipmentId sourceShipmentId, final ShipmentId shipmentId) {
        when(this.logisticsService.findByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());
        when(this.logisticsService.findByTargetAndType(DeliveryTarget.shipment(sourceShipmentId), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(shipmentDelivery(sourceShipmentId)));
    }

    private Delivery capturedDelivery() {
        final ArgumentCaptor<Delivery> saved = ArgumentCaptor.forClass(Delivery.class);
        verify(this.logisticsService).createOrUpdate(saved.capture());
        return saved.getValue();
    }

    @Test
    void shouldThrowExceptionWhenSourceDeliveryDoesNotExist() {
        final ShipmentId sourceShipmentId = new ShipmentId(10L);
        final ShipmentId shipmentId = new ShipmentId(11L);
        when(this.logisticsService.findByTargetAndType(any(DeliveryTarget.class), any(DeliveryType.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.logisticsPort.createDerivedDelivery(derivedCommand(shipmentId, sourceShipmentId,
                DeliveryDerivationType.SPLIT))).isInstanceOf(NoSuchElementException.class);
        verify(this.logisticsService, never()).createOrUpdate(any());
    }

    private CreateDerivedDeliveryCommand derivedCommand(final ShipmentId shipmentId, final ShipmentId sourceShipmentId,
                                                        final DeliveryDerivationType derivationType) {
        return new CreateDerivedDeliveryCommand(shipmentId, sourceShipmentId, derivationType, false, USER_ID);
    }

    private Delivery shipmentDelivery(final ShipmentId shipmentId) {
        return new Delivery(DeliveryTarget.shipment(shipmentId), shipmentId, DeliveryType.OUTBOUND,
                ORIGIN_DEPARTMENT_ID, DESTINATION_DEPARTMENT_ID, PickupMethod.PICKUP_POINT, DeliveryMethod.LOCKER,
                PICKUP_POINT_ID, DELIVERY_PICKUP_POINT_ID, null, false, USER_ID);
    }
}
