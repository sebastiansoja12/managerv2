package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.addressdetermination.AddressDeterminationService;
import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.route.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.route.infrastructure.api.event.ShipmentLogEvent;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.port.secondary.ShipmentPort;
import com.warehouse.shipment.domain.service.NotificationCreatorService;
import com.warehouse.shipment.domain.vo.Notification;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.PaymentMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShipmentAdapter implements ShipmentPort {

    private final ShipmentMapper shipmentMapper;

    private final ShipmentRepository parcelRepository;

    private final MailPort mailPort;

    private final NotificationMapper notificationMapper;

    private final PaypalPort paypalPort;

    private final PaymentMapper paymentMapper;

    private final NotificationCreatorService notificationCreatorService;

    private final RouteLogEventPublisher routeLogEventPublisher;

    private final AddressDeterminationService addressDeterminationService;

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

        final String fastestRoute = addressDeterminationService.findFastestRoute(parcel.getRecipient().getCity());

        parcel.setDestination(fastestRoute);

        final Notification notification = notificationCreatorService.createNotification(parcel, REROUTE_MESSAGE);

        final com.warehouse.mail.domain.vo.Notification mailNotification = notificationMapper.map(notification);

        mailPort.sendNotification(mailNotification);

        return parcelRepository.update(parcel);
    }

    private ShipmentResponse createParcel(ShipmentRequest request) {

        final Parcel parcel = shipmentMapper.map(request);

        final String fastestRoute = addressDeterminationService.findFastestRoute(parcel.getRecipient().getCity());

        parcel.setDestination(fastestRoute);

        final Long parcelId = parcelRepository.save(parcel);

        final com.warehouse.shipment.domain.vo.PaymentRequest paymentRequest =
                buildPaymentRequest(parcelId, parcel.getParcelType().getPrice());

        final PaymentRequest request1 = paymentMapper.map(paymentRequest);

        final PaymentResponse payment = paypalPort.payment(request1);

        final Notification notification = notificationCreatorService.createNotification(parcel,
                payment.getLink().getPaymentUrl());

        final com.warehouse.mail.domain.vo.Notification mailNotification = notificationMapper.map(notification);

        mailPort.sendNotification(mailNotification);

        return shipmentMapper.map(parcelId, payment);

    }

    private com.warehouse.shipment.domain.vo.PaymentRequest buildPaymentRequest(Long parcelId, double price) {
        final com.warehouse.shipment.domain.vo.PaymentRequest request =
                new com.warehouse.shipment.domain.vo.PaymentRequest();
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
