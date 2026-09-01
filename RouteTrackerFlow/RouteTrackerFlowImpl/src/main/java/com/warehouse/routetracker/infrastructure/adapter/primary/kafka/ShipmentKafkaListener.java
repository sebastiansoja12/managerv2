package com.warehouse.routetracker.infrastructure.adapter.primary.kafka;

import com.warehouse.routetracker.domain.model.ShipmentStatusStateChangeCommand;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentChangedIntegrationEvent;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.mapper.ShipmentKafkaEventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShipmentKafkaListener {

    private final RouteTrackerLogPort routeTrackerLogPort;
    private final ShipmentKafkaEventMapper shipmentKafkaEventMapper;

    public ShipmentKafkaListener(final RouteTrackerLogPort routeTrackerLogPort,
                                 final ShipmentKafkaEventMapper shipmentKafkaEventMapper) {
        this.routeTrackerLogPort = routeTrackerLogPort;
        this.shipmentKafkaEventMapper = shipmentKafkaEventMapper;
    }

    @KafkaListener(
            topics = "${manager.kafka.topics.shipment-events:shipment.events}",
            groupId = "${spring.kafka.consumer.group-id:route-tracker-flow}"
    )
    public void handle(final ShipmentChangedIntegrationEvent message) {
        final ShipmentStatusStateChangeCommand command = this.shipmentKafkaEventMapper.map(message);
        this.routeTrackerLogPort.createOrChangeShipmentState(command);
        log.info("Processed shipment event {} for {}", command.eventType(), command.shipmentId().value());
    }

}
