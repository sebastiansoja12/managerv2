package com.warehouse.routelogger.infrastructure.adapter.primary;

import com.warehouse.routelogger.domain.model.AnyDeliveryRequest;
import com.warehouse.routelogger.domain.model.Request;
import com.warehouse.routelogger.domain.model.SupplierCodeRequest;
import com.warehouse.routelogger.domain.port.primary.RouteLoggerPort;
import com.warehouse.routelogger.event.*;
import com.warehouse.routelogger.infrastructure.adapter.primary.mapper.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

import static org.mapstruct.factory.Mappers.getMapper;

@Component
@Slf4j
public class RouteLoggerListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSS");

    private final EventMapper eventMapper = getMapper(EventMapper.class);

    private final RouteLoggerPort routeLoggerPort;

    public RouteLoggerListener(RouteLoggerPort routeLoggerPort) {
        this.routeLoggerPort = routeLoggerPort;
    }

    @EventListener
    void handleEvent(DeliveryLogEvent event) {
        logEvent(event);
        final AnyDeliveryRequest request = eventMapper.map(event.getDeliveryRequest());
        routeLoggerPort.logAnyDelivery(request);
    }

    @EventListener
    void handleEvent(DepotCodeLogEvent event) {
        logEvent(event);
        final AnyDeliveryRequest request = eventMapper.mapFromDepotCodeRequest(event.getDepotCodeRequest());
        routeLoggerPort.logDepotCode(request);
    }

    @EventListener
    void handleEvent(RequestLogEvent event) {
        logEvent(event);
        final Request request = eventMapper.mapToRequest(event.getRequest());
        routeLoggerPort.logRequest(request);
    }

    @EventListener
    void handleEvent(SupplierCodeLogEvent event) {
        logEvent(event);
        final SupplierCodeRequest request = eventMapper.mapToSupplierCodeRequest(event.getSupplierCodeRequest());
        routeLoggerPort.logSupplierCode(request);
    }

    private void logEvent(RouteLogBaseEvent event) {
        log.info("Detected event {} at {}", event.getClass().getSimpleName(),
                event.getLocalDateTime().format(FORMATTER));
    }

}
