package com.warehouse.tracking.infrastructure.adapter.primary;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentStatusDto;
import com.warehouse.tracking.ShipmentStatusChanged;
import com.warehouse.tracking.infrastructure.adapter.primary.mapper.ShipmentEventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

import static org.mapstruct.factory.Mappers.getMapper;

@Component
@Slf4j
public class TrackingEventListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSS");

    private final ShipmentEventMapper eventMapper = getMapper(ShipmentEventMapper.class);

    @EventListener
    public void handle(final ShipmentStatusChanged event) {
        logEvent(event);
        final ShipmentDto shipment = event.getShipment();
        final ShipmentStatusDto shipmentStatus = shipment.getShipmentStatus();
        switch (shipmentStatus) {
            case REROUTE -> System.out.println("Reroute adapter");
            case REDIRECT -> System.out.println("Redirect adapter");
        }
    }

    void logEvent(final ShipmentStatusChanged event) {
        log.info("Detected event {} at {}", event.getClass().getSimpleName(),
                event.getLocalDateTime().format(FORMATTER));
    }
}