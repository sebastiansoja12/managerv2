package com.warehouse.routetracker.infrastructure.adapter.primary.kafka;

import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.time.LocalDateTime;
import java.nio.charset.StandardCharsets;

import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentChanged;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentCreated;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentReturned;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentSnapshot;

@ExtendWith(MockitoExtension.class)
class ShipmentKafkaListenerTest {

    private static final Long SHIPMENT_ID = 123L;
    private static final Instant EVENT_TIME = Instant.parse("2026-08-11T10:15:30Z");
    private static final LocalDateTime OCCURRED_AT = LocalDateTime.of(2026, 8, 11, 10, 15, 30);
    private static final UserId USER_ID = new UserId(42L);
    private static final DepartmentId DEPARTMENT_ID = new DepartmentId(10L);
    private static final OperatorId OPERATOR_ID = new OperatorId(7L);

    @Mock
    private RouteTrackerLogPort routeTrackerLogPort;

    private ObjectMapper objectMapper;
    private ShipmentKafkaListener listener;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
        this.listener = new ShipmentKafkaListener(this.objectMapper, this.routeTrackerLogPort);
    }

    @Test
    void shouldHandleShipmentCreated() throws Exception {
        final ShipmentCreated event = new ShipmentCreated(
                this.snapshot("CREATED"), EVENT_TIME, USER_ID, DEPARTMENT_ID, OPERATOR_ID);

        this.listener.handle(event);

        verify(this.routeTrackerLogPort).createShipmentEvent(
                new ShipmentId(SHIPMENT_ID),
                "ShipmentCreated",
                ShipmentStatus.CREATED,
                OCCURRED_AT,
                this.objectMapper.writeValueAsString(event),
                USER_ID,
                DEPARTMENT_ID
        );
    }

    @Test
    void shouldHandleShipmentReturned() throws Exception {
        final ShipmentReturned event = new ShipmentReturned(
                this.snapshot("RETURN"), EVENT_TIME, "DAMAGED", "Damaged package",
                USER_ID, DEPARTMENT_ID, OPERATOR_ID);

        this.listener.handle(event);

        verify(this.routeTrackerLogPort).createShipmentEvent(
                new ShipmentId(SHIPMENT_ID),
                "ShipmentReturned",
                ShipmentStatus.RETURN,
                OCCURRED_AT,
                this.objectMapper.writeValueAsString(event),
                USER_ID,
                DEPARTMENT_ID
        );
    }

    @Test
    void shouldHandleOtherShipmentEvent() throws Exception {
        final ShipmentChanged event = new ShipmentChanged(
                this.snapshot("SENT"), EVENT_TIME, USER_ID, DEPARTMENT_ID, OPERATOR_ID);

        this.listener.handle(
                event,
                "ShipmentSent".getBytes(StandardCharsets.UTF_8));

        verify(this.routeTrackerLogPort).createShipmentEvent(
                new ShipmentId(SHIPMENT_ID),
                "ShipmentSent",
                ShipmentStatus.SENT,
                OCCURRED_AT,
                this.objectMapper.writeValueAsString(event),
                USER_ID,
                DEPARTMENT_ID
        );
    }

    @Test
    void shouldHandleShipmentReturnCanceledEvent() throws Exception {
        final ShipmentChanged event = new ShipmentChanged(
                this.snapshot("DELIVERY"), EVENT_TIME, USER_ID, DEPARTMENT_ID, OPERATOR_ID);

        this.listener.handle(
                event,
                "ShipmentReturnCanceled".getBytes(StandardCharsets.UTF_8));

        verify(this.routeTrackerLogPort).createShipmentEvent(
                new ShipmentId(SHIPMENT_ID),
                "ShipmentReturnCanceled",
                ShipmentStatus.DELIVERY,
                OCCURRED_AT,
                this.objectMapper.writeValueAsString(event),
                USER_ID,
                DEPARTMENT_ID
        );
    }

    private ShipmentSnapshot snapshot(final String shipmentStatus) {
        return new ShipmentSnapshot(new ShipmentId(SHIPMENT_ID), shipmentStatus);
    }
}
