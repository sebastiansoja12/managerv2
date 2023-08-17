package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;

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

    private Parcel createParcel() {
        return Parcel.builder()
                .recipient(createRecipient())
                .parcelSize(Size.TEST)
                .sender(createSender())
                .status(Status.CREATED)
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
