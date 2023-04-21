package com.warehouse.shipment;

import com.warehouse.addressdetermination.AddressDeterminationService;
import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import com.warehouse.shipment.domain.enumeration.ParcelType;
import com.warehouse.shipment.domain.enumeration.Status;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.service.NotificationCreatorService;
import com.warehouse.shipment.domain.vo.Notification;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentAdapter;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShipmentAdapterTest {


    @Mock
    private ShipmentMapper shipmentMapper;
    @Mock
    private ShipmentRepository parcelRepository;
    @Mock
    private MailPort mailPort;
    @Mock
    private NotificationMapper notificationMapper;
    @Mock
    private PaypalPort paypalPort;

    @Mock
    private NotificationCreatorService notificationCreatorService;
    @Mock
    private RouteLogEventPublisher routeLogEventPublisher;
    @Mock
    private AddressDeterminationService addressDeterminationService;

    private ShipmentAdapter shipmentAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        shipmentAdapter = new ShipmentAdapter(shipmentMapper, parcelRepository, mailPort, notificationMapper,
                paypalPort, notificationCreatorService, routeLogEventPublisher, addressDeterminationService);
    }
    @Test
    public void shouldUpdateParcel() {
        // given
        final ParcelUpdate parcelUpdate = ParcelUpdate.builder().build();
        final Parcel parcel = new Parcel();
        // and: create recipient to obtain city
        parcel.setRecipient(createRecipient());

        final Notification notification = new Notification();
        final UpdateParcelResponse expectedResponse = new UpdateParcelResponse();
        expectedResponse.setParcel(createParcel());

        when(shipmentMapper.map(parcelUpdate)).thenReturn(parcel);
        when(addressDeterminationService
                .findFastestRoute(parcel.getRecipient().getCity())).thenReturn("fastest route");
        when(notificationCreatorService
                .createNotification(parcel, "Zmiana trasy przesyłki")).thenReturn(notification);
        when(notificationMapper.map(notification)).thenReturn(any());
        when(parcelRepository.update(parcel)).thenReturn(expectedResponse);

        // when
        final UpdateParcelResponse actualResponse = shipmentAdapter.update(parcelUpdate);

        // then
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    private Parcel createParcel() {
        return Parcel.builder()
                .recipient(createRecipient())
                .parcelType(ParcelType.TEST)
                .sender(createSender())
                .status(Status.REROUTE.name())
                .build();
    }

    private Recipient createRecipient() {
        return Recipient.builder()
                .firstName("test")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .build();
    }

    private Sender createSender() {
        return Sender.builder()
                .firstName("updatedTest")
                .lastName("test")
                .city("test")
                .street("test")
                .postalCode("00-000")
                .telephoneNumber("123")
                .email("test@test.pl")
                .build();
    }
}
