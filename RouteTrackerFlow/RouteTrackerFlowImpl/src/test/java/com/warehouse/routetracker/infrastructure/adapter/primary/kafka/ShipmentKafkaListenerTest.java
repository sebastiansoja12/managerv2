package com.warehouse.routetracker.infrastructure.adapter.primary.kafka;

import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.routetracker.domain.model.ShipmentStatusStateChangeCommand;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentStatus;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentSnapshot;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.TrackingNumber;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.mapper.ShipmentKafkaEventMapper;

@ExtendWith(MockitoExtension.class)
class ShipmentKafkaListenerTest {

    private static final LocalDateTime CHANGED_AT = LocalDateTime.of(2026, 8, 11, 10, 15, 30);

    @Mock
    private RouteTrackerLogPort routeTrackerLogPort;

    private ShipmentKafkaListener listener;

    @BeforeEach
    void setUp() {
        this.listener = new ShipmentKafkaListener(this.routeTrackerLogPort, new ShipmentKafkaEventMapper());
    }

    @Test
    void shouldMapLocalMessageToPrimaryCommand() {
        final ShipmentCreatedIntegrationEvent message = new ShipmentCreatedIntegrationEvent(
                new ShipmentSnapshot(
                        new ShipmentId(123L),
                        null,
                        null,
                        null,
                        null,
                        null,
                        ShipmentStatus.CREATED,
                        null,
                        null,
                        null,
                        CHANGED_AT,
                        CHANGED_AT,
                        false,
                        null,
                        false,
                        null,
                        null,
                        null,
                        null,
                        new TrackingNumber("TRACKING-123"),
                        null),
                new UserId(42L),
                new DepartmentId(10L),
                new OperatorId(7L)
        );

        this.listener.handle(message);

        verify(this.routeTrackerLogPort).createOrChangeShipmentState(new ShipmentStatusStateChangeCommand(
                new com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId(123L),
                "shipment.changed",
                com.warehouse.routetracker.domain.enumeration.ShipmentStatus.CREATED,
                CHANGED_AT,
                new OperatorId(7L),
                new DepartmentId(10L),
                new UserId(42L)
        ));
    }
}
