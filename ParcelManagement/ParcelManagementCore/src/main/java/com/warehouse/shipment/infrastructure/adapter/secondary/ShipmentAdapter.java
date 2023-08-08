package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.route.infrastructure.api.event.ShipmentLogEvent;
import com.warehouse.shipment.domain.model.ShipmentResponse;
import com.warehouse.shipment.domain.port.secondary.ShipmentServicePort;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentAdapter implements ShipmentServicePort {

    private final ShipmentMapper shipmentMapper;

    private final RouteLogEventPublisher routeLogEventPublisher;

    @Override
    public ShipmentResponse registerParcel(Long parcelId, String paymentUrl) {
        final ShipmentResponse shipmentResponse = new ShipmentResponse(paymentUrl, parcelId);
        sendEvent(shipmentResponse);
        return shipmentResponse;
    }

    public void sendEvent(ShipmentResponse shipmentResponse) {
        routeLogEventPublisher.send(buildShipmentEvent(shipmentResponse));
    }

    public ShipmentLogEvent buildShipmentEvent(ShipmentResponse shipmentResponse) {
        return ShipmentLogEvent.builder()
                .shipmentRequest(shipmentMapper.mapToRequestDto(shipmentResponse))
                .build();
    }

}
