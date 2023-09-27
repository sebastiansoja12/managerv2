package com.warehouse.delivery.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Set;

import com.warehouse.delivery.domain.model.DeliveryRouteRequest;
import com.warehouse.delivery.domain.model.DeliveryRouteResponse;
import com.warehouse.delivery.domain.port.secondary.RouteLogServicePort;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryMapper;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.RouteLogEventPublisher;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.dto.SupplyInformationDto;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.event.SupplyLogEvent;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RouteLogAdapter implements RouteLogServicePort {


    private final RouteLogEventPublisher routeLogEventPublisher;

    private final DeliveryMapper deliveryMapper;


    @Override
    public List<DeliveryRouteResponse> deliver(Set<DeliveryRouteRequest> deliveryRequest) {
        final List<SupplyInformationDto> supplyInformationDto = deliveryMapper.map(deliveryRequest);
        sendEvent(buildEvent(supplyInformationDto));
        return deliveryRequest.stream()
                .map(deliveryMapper::map)
                .toList();
    }


    private SupplyLogEvent buildEvent(List<SupplyInformationDto> supplyInformation) {
        return SupplyLogEvent.builder()
                .supplyInformation(supplyInformation)
                .build();
    }

    private void sendEvent(SupplyLogEvent event) {
        routeLogEventPublisher.send(event);
    }
}
