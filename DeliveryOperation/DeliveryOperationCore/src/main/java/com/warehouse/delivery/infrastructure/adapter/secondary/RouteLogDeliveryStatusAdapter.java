package com.warehouse.delivery.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.Set;

import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.port.secondary.RouteLogDeliveryStatusServicePort;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryEventMapper;
import com.warehouse.routelogger.RouteLogEventPublisher;
import com.warehouse.routelogger.event.DeliveryLogEvent;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RouteLogDeliveryStatusAdapter implements RouteLogDeliveryStatusServicePort {


    private final RouteLogEventPublisher routeLogEventPublisher;

    private final DeliveryEventMapper eventMapper = getMapper(DeliveryEventMapper.class);

    @Override
    public void deliver(Set<Delivery> deliveryRequest) {
        deliveryRequest.forEach(delivery -> sendEvent(buildEvent(delivery)));
    }

    private DeliveryLogEvent buildEvent(Delivery delivery) {
        return DeliveryLogEvent.builder()
                .build();
    }

    private void sendEvent(DeliveryLogEvent event) {
        routeLogEventPublisher.send(event);
    }

}
