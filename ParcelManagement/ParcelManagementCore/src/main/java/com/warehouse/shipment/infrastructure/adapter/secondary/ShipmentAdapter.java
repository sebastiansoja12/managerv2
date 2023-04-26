package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.depot.api.DepotService;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.route.infrastructure.api.event.ShipmentLogEvent;
import com.warehouse.shipment.domain.enumeration.ParcelType;
import com.warehouse.shipment.domain.enumeration.Status;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.ShipmentPort;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.service.NotificationCreatorService;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapper;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ShipmentAdapter implements ShipmentPort {

    private final ShipmentMapper shipmentMapper;

    private final ShipmentRepository parcelRepository;

    private final MailPort mailPort;

    private final NotificationMapper notificationMapper;

    private final PaypalPort paypalPort;

    private final NotificationCreatorService notificationCreatorService;

    private final RouteLogEventPublisher routeLogEventPublisher;

    private final VoronoiService voronoiService;

    private final DepotService depotService;

    private final String REROUTE_MESSAGE = "Zmiana trasy przesyłki";

    @Override
    public ShipmentResponse ship(ShipmentRequest request) {
        final ShipmentResponse shipmentResponse =  createParcel(request);
        sendEvent(shipmentResponse);
        return shipmentResponse;
    }

    @Override
    public UpdateParcelResponse update(ParcelUpdate parcelUpdate) {

        final Parcel parcel = shipmentMapper.map(parcelUpdate);

        final List<DepotDto> depots = depotService.findAll();

        final String fastestRoute = voronoiService.findFastestRoute(depots, parcel.getRecipient().getCity());

        parcel.setDestination(fastestRoute);

        parcel.setStatus(Status.REROUTE);

        final Notification notification = notificationCreatorService.createNotification(parcel, REROUTE_MESSAGE);

        mailPort.sendNotification(notification);

        return parcelRepository.update(parcel);
    }

    private ShipmentResponse createParcel(ShipmentRequest request) {

        final Parcel parcel = request.getParcel();

        final List<DepotDto> depots = depotService.findAll();

        final String fastestRoute = voronoiService.findFastestRoute(depots, parcel.getRecipient().getCity());

        parcel.setDestination(fastestRoute);

        parcel.setStatus(Status.CREATED);

        parcel.setParcelType(ParcelType.PARENT);

        final Long parcelId = parcelRepository.save(parcel);

        final PaymentRequest paymentRequest = buildPaymentRequest(parcelId, parcel.getParcelSize().getPrice());

        final PaymentResponse payment = paypalPort.payment(paymentRequest);

        final Notification notification = notificationCreatorService.createNotification(parcel,
                payment.getLink().getPaymentUrl());

        mailPort.sendNotification(notification);

        return shipmentMapper.map(parcelId, payment);

    }

    private PaymentRequest buildPaymentRequest(Long parcelId, double price) {
        final PaymentRequest request = new PaymentRequest();
        request.setParcelId(parcelId);
        request.setPrice(price);

        return request;
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
