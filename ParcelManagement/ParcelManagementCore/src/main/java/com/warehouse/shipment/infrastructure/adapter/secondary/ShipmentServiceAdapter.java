package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.route.infrastructure.api.event.ShipmentLogEvent;
import com.warehouse.shipment.domain.model.ParcelUpdate;
import com.warehouse.shipment.domain.model.ShipmentResponse;
import com.warehouse.shipment.domain.model.UpdateParcelResponse;
import com.warehouse.shipment.domain.port.secondary.ShipmentServicePort;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentServiceAdapter implements ShipmentServicePort {

    private final ShipmentMapper shipmentMapper;

    private final RouteLogEventPublisher routeLogEventPublisher;

    @Override
    public ShipmentResponse registerParcel(Long parcelId, String paymentUrl) {
        final ShipmentResponse shipmentResponse = new ShipmentResponse(paymentUrl, parcelId);
        sendEvent(shipmentResponse);
        return shipmentResponse;
    }

    @Override
    public UpdateParcelResponse update(ParcelUpdate parcelUpdate) {

        //final Parcel parcel = shipmentMapper.map(parcelUpdate);

        //final List<DepotDto> depots = depotService.findAll();

        //final String fastestRoute = voronoiService.findFastestRoute(depots, parcel.getRecipient().getCity());

        //parcel.setDestination(fastestRoute);

        //parcel.setStatus(Status.REROUTE);

        //final Notification notification = notificationCreatorProvider.createNotification(parcel, REROUTE_MESSAGE);

        //mailPort.sendNotification(notification);

        return null;
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
