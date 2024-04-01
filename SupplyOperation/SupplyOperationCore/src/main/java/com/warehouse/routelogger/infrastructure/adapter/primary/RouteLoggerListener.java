package com.warehouse.routelogger.infrastructure.adapter.primary;

import com.warehouse.routelogger.domain.model.AnyDeliveryRequest;
import com.warehouse.routelogger.domain.port.primary.RouteLoggerPort;
import com.warehouse.routelogger.event.DeliveryMissedLogEvent;
import com.warehouse.routelogger.event.RouteLogBaseEvent;
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
    void handleEvent(DeliveryMissedLogEvent event) {
        logEvent(event);
        final AnyDeliveryRequest request = eventMapper.map(event.getDeliveryMissedRequest());
        routeLoggerPort.logAnyDelivery(request);
    }

    private void logEvent(RouteLogBaseEvent event) {
        log.info("Detected event {} at {}", event.getClass().getSimpleName(),
                event.getLocalDateTime().format(FORMATTER));
    }

}
