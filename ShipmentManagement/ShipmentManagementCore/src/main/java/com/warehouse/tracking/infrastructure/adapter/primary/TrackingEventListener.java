package com.warehouse.tracking.infrastructure.adapter.primary;

import java.time.format.DateTimeFormatter;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.redirect.RedirectService;
import com.warehouse.reroute.RerouteService;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentStatusDto;
import com.warehouse.tracking.ShipmentStatusChanged;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TrackingEventListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSS");

    private final RerouteService rerouteService;

    private final RedirectService redirectService;

    public TrackingEventListener(final RerouteService rerouteService, final RedirectService redirectService) {
        this.rerouteService = rerouteService;
        this.redirectService = redirectService;
    }

    @EventListener
    public void handle(final ShipmentStatusChanged event) {
        logEvent(event);
        final ShipmentIdDto shipmentId = event.getShipmentId();
        final ShipmentStatusDto shipmentStatus = event.getShipmentStatus();
        switch (shipmentStatus) {
            case REROUTE -> rerouteService.processRerouting(shipmentId);
            case REDIRECT -> redirectService.processRedirecting(shipmentId);
            default -> log.info("Shipment not eligible for status change");
        }
    }

    void logEvent(final ShipmentStatusChanged event) {
        log.info("Detected event {} at {}", event.getClass().getSimpleName(),
                event.getLocalDateTime().format(FORMATTER));
    }
}
