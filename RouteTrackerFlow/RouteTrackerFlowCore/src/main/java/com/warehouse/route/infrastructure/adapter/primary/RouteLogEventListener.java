package com.warehouse.route.infrastructure.adapter.primary;

import com.warehouse.route.domain.model.RouteRequest;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.ShipmentRequest;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.route.domain.vo.SupplyInformation;
import com.warehouse.route.infrastructure.adapter.primary.mapper.EventMapper;
import com.warehouse.route.infrastructure.api.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class RouteLogEventListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSS");

    private final EventMapper eventMapper;

    private final RouteTrackerLogPort trackerLogPort;


    @EventListener
    public void handle(ShipmentLogEvent event) {
        logEvent(event);
        final ShipmentRequest shipmentRequest = eventMapper.map(event.getShipmentRequest());
        trackerLogPort.initializeRoute(shipmentRequest.getParcelId());
    }

    @EventListener
    public void handle(SupplyLogEvent event) {
        logEvent(event);
        final SupplyInformation supplyInformation = eventMapper.map(event.getSupplyInformation());
        trackerLogPort.saveSupplyRoute(supplyInformation);
    }

    @EventListener
    public void handle(RouteRequestEvent event) {
        logEvent(event);
        final RouteRequest routeRequest = eventMapper.map(event.getRouteRequestDto());
        trackerLogPort.saveRoute(routeRequest);
    }

    @EventListener
    public String handle(RouteResponseEvent event) {
        logEvent(event);
        final RouteResponse routeResponse = eventMapper.map(event.getRouteResponse());
        return routeResponse.getId().toString();
    }

    @EventListener
    public void handle(RouteRequestsEvent event) {
        logEvent(event);
        final List<RouteRequest> routeRequest = eventMapper.mapToMultipleRouteRequests(event.getRequest());
        trackerLogPort.saveMultipleRoutes(routeRequest);
    }

    private void logEvent(RouteLogBaseEvent event) {
        log.info("Detected event " + event.getClass().getSimpleName() + " at " +
                event.getLocalDateTime().format(FORMATTER));
    }
}
