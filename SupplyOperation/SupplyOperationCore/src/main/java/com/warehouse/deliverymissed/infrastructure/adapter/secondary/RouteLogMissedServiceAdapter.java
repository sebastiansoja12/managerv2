package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import com.warehouse.deliverymissed.domain.port.secondary.RouteLogMissedServicePort;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper.DeliveryMissedEventMapper;
import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.routelogger.event.DeliveryLogEvent;
import com.warehouse.routelogger.event.DepotCodeLogEvent;
import lombok.AllArgsConstructor;

import static org.mapstruct.factory.Mappers.getMapper;


@AllArgsConstructor
public class RouteLogMissedServiceAdapter implements RouteLogMissedServicePort {


    private final RouteLogEventPublisher routeLogEventPublisher;

    private final DeliveryMissedEventMapper eventMapper = getMapper(DeliveryMissedEventMapper.class);


    @Override
    public void logRouteLogMissedDelivery(DeliveryMissed deliveryMissed) {
        sendEvent(buildDeliveryLogEvent(deliveryMissed));
    }

    @Override
    public void logDepotCodeMissedDelivery(DeliveryMissed deliveryMissed) {
        sendEvent(buildDepotCodeLogEvent(deliveryMissed));
    }

    private DepotCodeLogEvent buildDepotCodeLogEvent(DeliveryMissed deliveryMissed) {
        return DepotCodeLogEvent.builder()
                .depotCodeRequest(eventMapper.mapToDepotCodeRequest(deliveryMissed))
                .build();
    }

    private DeliveryLogEvent buildDeliveryLogEvent(DeliveryMissed deliveryMissed) {
        return DeliveryLogEvent.builder()
                .deliveryRequest(eventMapper.map(deliveryMissed))
                .build();
    }

    private void sendEvent(RouteLogEvent event) {
        routeLogEventPublisher.send(event);
    }
}
