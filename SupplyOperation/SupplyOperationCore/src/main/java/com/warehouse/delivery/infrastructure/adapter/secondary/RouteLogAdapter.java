package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.port.secondary.RouteLogServicePort;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.DeliveryMapper;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.route.infrastructure.api.dto.SupplyInformationDto;
import com.warehouse.route.infrastructure.api.event.SupplyLogEvent;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class RouteLogAdapter implements RouteLogServicePort {


    private final RouteLogEventPublisher routeLogEventPublisher;

    private final DeliveryMapper deliveryMapper;

    // TODO INPL-8007
    @Override
    public List<DeliveryResponse> deliver(Set<DeliveryRequest> deliveryRequest) {
        final List<SupplyInformationDto> supplyInformationDto = null;
        sendEvent(buildEvent(supplyInformationDto));
        return null;
    }



    public SupplyLogEvent buildEvent(List<SupplyInformationDto> supplyInformation) {
        return SupplyLogEvent.builder()
                .supplyInformation(supplyInformation)
                .build();
    }

    public void sendEvent(SupplyLogEvent event) {
        routeLogEventPublisher.send(event);
    }
}
