package com.warehouse.route.infrastructure.adapter.primary;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.route.domain.model.ShipmentRequest;
import com.warehouse.route.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.route.domain.vo.DeliveryInformation;
import com.warehouse.route.infrastructure.adapter.primary.mapper.EventMapper;
import com.warehouse.route.infrastructure.api.event.DeliveryLogEvent;
import com.warehouse.route.infrastructure.api.event.RouteLogBaseEvent;
import com.warehouse.route.infrastructure.api.event.ShipmentLogEvent;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RouteLogEventListener {

    private final RouteTrackerLogPort trackerLogPort;

    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

    private final Logger logger = LoggerFactory.getLogger(RouteLogEventListener.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSS");

    @EventListener
    public void handle(ShipmentLogEvent event) {
        logEvent(event);
        final ShipmentRequest shipmentRequest = eventMapper.map(event.getShipmentRequest());
        trackerLogPort.initializeRoute(shipmentRequest.getParcelId());
    }

    @EventListener
    public void handle(DeliveryLogEvent event) {
        logEvent(event);
        final List<DeliveryInformation> deliveryInformation = eventMapper.map(event.getDeliveryInformation());
        trackerLogPort.saveDelivery(deliveryInformation);
    }

    private void logEvent(RouteLogBaseEvent event) {
		logger.info("Detected event {} at {}", event.getClass().getSimpleName(),
				event.getLocalDateTime().format(FORMATTER));
    }
}
