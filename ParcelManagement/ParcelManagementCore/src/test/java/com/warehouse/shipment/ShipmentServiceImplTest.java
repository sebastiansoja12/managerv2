package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.exception.DestinationDepotDeterminationException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.secondary.*;
import com.warehouse.shipment.domain.service.NotificationCreatorProvider;
import com.warehouse.shipment.domain.service.ShipmentServiceImpl;
import com.warehouse.shipment.domain.vo.Notification;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ParcelNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceImplTest {


    @Mock
    private ShipmentServicePort shipmentServicePort;
    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private PathFinderServicePort pathFinderServicePort;
    @Mock
    private PaypalServicePort paypalServicePort;
    @Mock
    private NotificationCreatorProvider notificationCreatorProvider;
    @Mock
    private MailServicePort mailServicePort;
    @Mock
    private Logger logger;
    private ShipmentServiceImpl service;

	@BeforeEach
	void setup() {
		service = new ShipmentServiceImpl(shipmentServicePort, shipmentRepository, pathFinderServicePort,
				paypalServicePort, notificationCreatorProvider, mailServicePort, logger);
	}

    @Test
    void shouldCreateShip() {
        // given
        final ShipmentParcel shipmentParcel = createShipmentParcel();

        final City city = new City("Katowice");

        final Parcel parcel = createParcel();

        final ShipmentResponse expectedResponse = new ShipmentResponse("paymentUrl", 1L);

        final PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setPaymentMethod("paypal");
        paymentStatus.setLink("paymentUrl");

        final Notification notification = new Notification("test",
                "test@test.pl", "test");

        doReturn(city)
                .when(pathFinderServicePort)
                .determineDeliveryDepot(any(Address.class));

        doReturn(parcel)
                .when(shipmentRepository)
                .save(shipmentParcel);

        doReturn(paymentStatus)
                .when(paypalServicePort)
                .payment(parcel);

        doReturn(notification)
                .when(notificationCreatorProvider)
                .createNotification(any(), any());

        doNothing()
                .when(mailServicePort)
                .sendShipmentNotification(notification);

        when(shipmentServicePort.registerParcel(1L, "paymentUrl")).thenReturn(
                expectedResponse);


        // when
        final ShipmentResponse response = service.createShipment(shipmentParcel);
        // then
        assertAll(
                () -> assertEquals(response.getParcelId(), expectedToBe(1L)),
                () -> assertEquals(response.getPaymentUrl(), expectedToBe("paymentUrl"))
        );
    }


    @Test
    void shouldNotCreateShipmentWhenDestinationDepotIsNotDetermined() {
        // given
        final ShipmentParcel shipmentParcel = createShipmentParcel();

        doReturn(new City(null))
                .when(pathFinderServicePort)
                .determineDeliveryDepot(any(Address.class));
        // when
        final Executable executable = () -> service.createShipment(shipmentParcel);
        // then
        final DestinationDepotDeterminationException exception =
                assertThrows(DestinationDepotDeterminationException.class, executable);

        assertEquals(exception.getMessage(),
                expectedToBe("Delivery depot could not be determined"));
    }

    @Test
    void shouldLoadParcel() {
        // given
        final Long parcelId = 1L;

        // parcel with id 1L
        final Parcel expectedParcel = new Parcel();
        expectedParcel.setId(1L);

        doReturn(expectedParcel)
                .when(shipmentRepository)
                .loadParcelById(parcelId);
        // when
        final Parcel parcel = service.loadParcel(parcelId);

        // then
        assertEquals(parcel.getId(), expectedToBe(1L));
        verify(shipmentRepository, times(1)).loadParcelById(parcelId);
    }

    @Test
    void shouldNotLoadParcelAndThrowException() {
        // given
        final Long parcelId = 1L;

        // parcel with id 1L
        final Parcel expectedParcel = new Parcel();
        expectedParcel.setId(1L);

        // build exception to throw
        final ParcelNotFoundException expectedException = new ParcelNotFoundException("Parcel was not found");

        doThrow(expectedException)
                .when(shipmentRepository)
                .loadParcelById(parcelId);
        // when
        final Executable executable = () -> service.loadParcel(parcelId);

        // then
        final ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, executable);
        assertEquals(exception.getMessage(), expectedToBe("Parcel was not found"));
    }

    @Test
    void shouldUpdateParcel() {
        // TODO
    }

    @Test
    void shouldDeleteParcelById() {
        // given
        final Long parcelId = 1L;
        // when
        service.delete(parcelId);
        // then
        verify(shipmentRepository, times(1)).delete(parcelId);
    }

    private <T> T expectedToBe(T value) {
        return value;
    }

    private Parcel createParcel() {
        return Parcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .status(Status.CREATED)
                .id(1L)
                .build();
    }

    private ShipmentParcel createShipmentParcel() {
        return ShipmentParcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .status(Status.CREATED)
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
