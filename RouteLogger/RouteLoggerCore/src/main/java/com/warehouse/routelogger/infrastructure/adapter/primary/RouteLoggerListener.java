package com.warehouse.routelogger.infrastructure.adapter.primary;

import com.warehouse.routelogger.domain.model.*;
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

    public RouteLoggerListener(final RouteLoggerPort routeLoggerPort) {
        this.routeLoggerPort = routeLoggerPort;
    }

    @EventListener
    void handleEvent(final DeliveryLogEvent event) {
        logEvent(event);
        final AnyDeliveryRequest request = eventMapper.map(event.getDeliveryRequest());
        routeLoggerPort.logAnyDelivery(request);
    }

    @EventListener
    void handleEvent(final DepotCodeLogEvent event) {
        logEvent(event);
        final DepotCodeRequest request = eventMapper.mapFromDepotCodeRequest(event.getDepotCodeRequest());
        routeLoggerPort.logDepotCode(request);
    }

    @EventListener
    void handleEvent(final RequestLogEvent event) {
        logEvent(event);
        final Request request = eventMapper.mapToRequest(event.getRequest());
        routeLoggerPort.logRequest(request);
    }

    @EventListener
    void handleEvent(final SupplierCodeLogEvent event) {
        logEvent(event);
        final SupplierCodeRequest request = eventMapper.mapToSupplierCodeRequest(event.getSupplierCodeRequest());
        routeLoggerPort.logSupplierCode(request);
    }

    @EventListener
    void handleEvent(final TerminalLogEvent event) {
        logEvent(event);
        final TerminalLogRequest request = eventMapper.mapToTerminalLogRequest(event.getTerminalLogRequest());
        routeLoggerPort.logTerminalId(request);
    }

    @EventListener
    void handleEvent(final VersionLogEvent event) {
        logEvent(event);
        final VersionLogRequest request = eventMapper.mapToVersionLogRequest(event.getVersionLogRequest());
        routeLoggerPort.logVersion(request);
    }

    @EventListener
    void handleEvent(final UsernameLogEvent event) {
        logEvent(event);
        final UsernameLogRequest request = eventMapper.mapToUsernameLogRequest(event.getUsernameLogRequest());
        routeLoggerPort.logUsername(request);
    }

    private void logEvent(final RouteLogBaseEvent event) {
        log.info("Detected event {} at {}", event.getClass().getSimpleName(),
                event.getLocalDateTime().format(FORMATTER));
    }

}
