package com.warehouse.delivery.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Set;

import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.model.DeliveryRouteResponse;
import com.warehouse.delivery.domain.port.secondary.RouteLogServicePort;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryMapper;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.route.infrastructure.api.dto.DeliveryInformationDto;
import com.warehouse.route.infrastructure.api.event.DeliveryLogEvent;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RouteLogAdapter implements RouteLogServicePort {


    private final RouteLogEventPublisher routeLogEventPublisher;

    private final DeliveryMapper deliveryMapper;


    @Override
    public List<DeliveryRouteResponse> deliver(Set<DeliveryRouteRequest> deliveryRequest) {
        final List<DeliveryInformationDto> deliveryInformationDto = deliveryMapper.map(deliveryRequest);
        sendEvent(buildEvent(deliveryInformationDto));
        return deliveryRequest.stream()
                .map(deliveryMapper::map)
                .toList();
    }


    private DeliveryLogEvent buildEvent(List<DeliveryInformationDto> deliveryInformation) {
        return DeliveryLogEvent.builder()
                .deliveryInformation(deliveryInformation)
                .build();
    }

    private void sendEvent(DeliveryLogEvent event) {
        routeLogEventPublisher.send(event);
    }
}
