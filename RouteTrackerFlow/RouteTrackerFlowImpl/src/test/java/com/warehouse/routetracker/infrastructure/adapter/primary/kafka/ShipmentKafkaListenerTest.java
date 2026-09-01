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
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentChangedIntegrationEvent;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentEventData;
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
        final ShipmentChangedIntegrationEvent message = new ShipmentChangedIntegrationEvent(
                shipmentEventData(),
                "shipment.created",
                new OperatorId(7L),
                new DepartmentId(10L),
                new UserId(42L)
        );

        this.listener.handle(message);

        verify(this.routeTrackerLogPort).createOrChangeShipmentState(new ShipmentStatusStateChangeCommand(
                new ShipmentId(123L),
                "shipment.created",
                com.warehouse.routetracker.domain.enumeration.ShipmentStatus.CREATED,
                CHANGED_AT,
                new OperatorId(7L),
                new DepartmentId(10L),
                new UserId(42L)
        ));
    }

    private ShipmentEventData shipmentEventData() {
        return new ShipmentEventData(
                new com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentId(123L),
                null,
                null,
                null,
                null,
                null,
                com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentStatus.CREATED,
                null,
                null,
                null,
                null,
                CHANGED_AT,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
