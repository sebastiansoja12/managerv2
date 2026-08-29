package com.warehouse.logistics;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.logistics.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.DeliveryStatus;
import com.warehouse.logistics.domain.enumeration.DeliveryType;
import com.warehouse.logistics.domain.model.Delivery;
import com.warehouse.logistics.domain.model.DeliveryStep;
import com.warehouse.logistics.domain.model.DeliveryTarget;
import com.warehouse.logistics.domain.port.primary.LogisticsPortImpl;
import com.warehouse.logistics.domain.service.LogisticsService;
import com.warehouse.logistics.infrastructure.adapter.primary.mapper.DeliveryResponseMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeliveryManagementTest {

    @Test
    void shouldInitializeNewDeliveryInCreationConstructor() {
        final Delivery delivery = delivery();

        assertNotNull(delivery.getDeliveryId());
        assertNotNull(delivery.getCreatedAt());
        assertEquals(DeliveryLifecycleStatus.CREATED, delivery.getStatus());
        assertEquals(DeliveryStatus.DEPOT, delivery.getDeliveryStatus());
        assertEquals(1, delivery.getDeliverySteps().size());
        assertEquals(DeliveryStatus.DEPOT, delivery.getDeliverySteps().get(0).deliveryStatus());
    }

    @Test
    void shouldUseCreationTimestampForShipmentStep() {
        final ShipmentId shipmentId = new ShipmentId(1L);
        final Delivery delivery = new Delivery(DeliveryTarget.shipment(shipmentId), shipmentId,
                DeliveryType.OUTBOUND, DeliveryMethod.COURIER, null, null, null, false, new UserId(42L));

        assertEquals(DeliveryLifecycleStatus.CREATED, delivery.getStatus());
        assertEquals(DeliveryStatus.DEPOT, delivery.getDeliveryStatus());
        assertEquals(delivery.getCreatedAt(), delivery.getDeliverySteps().get(0).attemptedAt());
        assertEquals(delivery.getDeliveryId(), delivery.getDeliverySteps().get(0).deliveryId());
        assertEquals(new UserId(42L), delivery.getDeliverySteps().get(0).userId());
        assertEquals("42", new DeliveryResponseMapper().map(delivery).steps().get(0).userId());
    }

    @Test
    void shouldRejectStepBelongingToAnotherDelivery() {
        final Delivery delivery = delivery();
        final DeliveryStep step = DeliveryStep.attempt(DeliveryId.generate(), 1,
                LocalDateTime.now(), DeliveryStatus.DEPOT, null, null, null, null, null, null, null);

        assertThrows(IllegalArgumentException.class, () -> delivery.addStep(step));
    }

    @Test
    void shouldChangeMethodAndPersistDelivery() {
        final LogisticsService logisticsService = mock(LogisticsService.class);
        final LogisticsPortImpl logisticsPort = new LogisticsPortImpl(logisticsService);
        final Delivery delivery = delivery();
        when(logisticsService.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));

        final Delivery updated = logisticsPort.changeDeliveryMethod(delivery.getDeliveryId(), DeliveryMethod.COURIER);

        assertSame(delivery, updated);
        assertEquals(DeliveryMethod.COURIER, updated.getMethod());
        assertEquals(DeliveryLifecycleStatus.MODIFIED, updated.getStatus());
        verify(logisticsService).createOrUpdate(delivery);
    }

    @Test
    void shouldCancelAndPersistDelivery() {
        final LogisticsService logisticsService = mock(LogisticsService.class);
        final LogisticsPortImpl logisticsPort = new LogisticsPortImpl(logisticsService);
        final Delivery delivery = delivery();
        when(logisticsService.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));

        logisticsPort.cancelDelivery(delivery.getDeliveryId());

        assertEquals(DeliveryLifecycleStatus.CANCELED, delivery.getStatus());
        verify(logisticsService).createOrUpdate(delivery);
    }

    @Test
    void shouldRejectCancelingCompletedDelivery() {
        final LogisticsService logisticsService = mock(LogisticsService.class);
        final LogisticsPortImpl logisticsPort = new LogisticsPortImpl(logisticsService);
        final Delivery delivery = delivery();
        delivery.addStep(com.warehouse.logistics.domain.model.DeliveryStep.attempt(delivery.getDeliveryId(), 2,
                java.time.LocalDateTime.now(), com.warehouse.logistics.domain.enumeration.DeliveryStatus.DELIVERED,
                null, null, null, null, null, null, null));
        when(logisticsService.findById(delivery.getDeliveryId())).thenReturn(Optional.of(delivery));

        assertThrows(IllegalStateException.class, () -> logisticsPort.cancelDelivery(delivery.getDeliveryId()));
    }

    @Test
    void shouldReportMissingDelivery() {
        final LogisticsService logisticsService = mock(LogisticsService.class);
        final LogisticsPortImpl logisticsPort = new LogisticsPortImpl(logisticsService);
        final DeliveryId deliveryId = DeliveryId.generate();
        when(logisticsService.findById(deliveryId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> logisticsPort.findDelivery(deliveryId));
    }

    private Delivery delivery() {
        final ShipmentId shipmentId = new ShipmentId(1L);
        return new Delivery(DeliveryTarget.shipment(shipmentId), shipmentId, DeliveryType.OUTBOUND,
                null, null, null, null, false, null);
    }
}
