package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.shipment.infrastructure.adapter.secondary.exception.ParcelNotFoundException;

@ExtendWith(MockitoExtension.class)
class ShipmentPrimaryPortTest {

    @Mock
    private ShipmentService shipmentService;

    private Logger logger;

    private ShipmentPortImpl shipmentPort;

    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);
        shipmentPort = new ShipmentPortImpl(shipmentService, logger);
    }

    @Test
    void shouldShip() {
        // given
        final ShipmentRequest request = ShipmentRequest.builder()
                .parcel(createShipmentParcel())
                .build();

        // create expected response from service
        final ShipmentResponse expectedResponse = new ShipmentResponse("paymentUrl", 1L);

        when(shipmentService.createShipment(request.getParcel())).thenReturn(expectedResponse);
        // when
        final ShipmentResponse response = shipmentPort.ship(request);
        // then
        assertEquals(response, expectedToBeEqualTo(response));
    }

    @Test
    void shouldLoadParcel() {
        // given
        final Long parcelId = 100001L;

        doReturn(new Parcel())
                .when(shipmentService)
                .loadParcel(parcelId);
        // when
        final Parcel parcel = shipmentPort.loadParcel(parcelId);
        // then
        assertThat(parcel).isNotNull();
    }

    @Test
    void shouldNotLoadParcel() {
        // given
        final Long parcelId = 0L;
        final ParcelNotFoundException parcelNotFoundException = new ParcelNotFoundException("Parcel was not found");
        doThrow(parcelNotFoundException)
                .when(shipmentService)
                .loadParcel(parcelId);
        // when
        final Executable executable = () -> shipmentPort.loadParcel(parcelId);
        // then
        final ParcelNotFoundException exception =
                assertThrows(ParcelNotFoundException.class, executable);
        assertEquals(expectedToBe("Parcel was not found"), exception.getMessage());
    }

    private <T> T expectedToBe(T value) {
        return value;
    }

    private Parcel createParcel() {
        return Parcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .parcelStatus(Status.CREATED)
                .destination("KT1")
                .id(1L)
                .build();
    }

    private ShipmentParcel createShipmentParcel() {
        return ShipmentParcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .status(Status.CREATED)
                .destination("KT1")
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

    private <T> T expectedToBeEqualTo(T value) {
        return value;
    }
}
