package com.warehouse.routetracker.infrastructure.adapter.primary.kafka;

import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.model.CreateShipmentEventCommand;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentEventMessage;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentSnapshot;

@ExtendWith(MockitoExtension.class)
class ShipmentKafkaListenerTest {

    private static final UUID EVENT_ID = UUID.fromString("f783d37e-06e9-4efc-8f18-099343b150e8");
    private static final Instant EVENT_TIME = Instant.parse("2026-08-11T10:15:30Z");

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
    void shouldMapLocalMessageToPrimaryCommand() throws Exception {
        final ShipmentEventMessage message = new ShipmentEventMessage(
                EVENT_ID,
                "shipment.created",
                1,
                EVENT_TIME,
                new ShipmentSnapshot(
                        new com.warehouse.commonassets.identificator.ShipmentId(123L),
                        null,
                        null,
                        null,
                        null,
                        null,
                        com.warehouse.commonassets.enumeration.ShipmentStatus.CREATED,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        new TrackingNumber("TRACKING-123"),
                        null),
                new UserId(42L),
                new DepartmentId(10L),
                OperatorId.of(7L)
        );

        this.listener.handle(message);

        verify(this.routeTrackerLogPort).createShipmentEvent(new CreateShipmentEventCommand(
                EVENT_ID,
                new ShipmentId(123L),
                "shipment.created",
                ShipmentStatus.CREATED,
                LocalDateTime.of(2026, 8, 11, 10, 15, 30),
                this.objectMapper.writeValueAsString(message),
                new UserId(42L),
                new DepartmentId(10L)
        ));
    }
}
