package com.warehouse.shipment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.exception.RerouteTokenNotFoundException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.primary.ShipmentReroutePortImpl;
import com.warehouse.shipment.domain.service.ShipmentService;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;

@ExtendWith(MockitoExtension.class)
public class ShipmentReroutePortImplTest {



    @Mock
    private ShipmentService service;

    private ShipmentReroutePortImpl shipmentReroutePort;

    private final Integer VALID_TOKEN = 12345;

    @BeforeEach
    void setup() {
        shipmentReroutePort = new ShipmentReroutePortImpl(service);
    }

    @Test
    void shouldUpdateParcel() {
        // given
        final UpdateParcelRequest updateParcelRequest = new UpdateParcelRequest();
        updateParcelRequest.setParcel(createParcel());
        updateParcelRequest.setToken(VALID_TOKEN);

        final Parcel parcel =  createParcel();
        parcel.setStatus(Status.REROUTE);

        final UpdateParcelResponse expectedResponse = new UpdateParcelResponse();
        expectedResponse.setParcel(parcel);

        when(service.update(any(ParcelUpdate.class))).thenReturn(expectedResponse);
        // when
        final UpdateParcelResponse response = shipmentReroutePort.reroute(updateParcelRequest);

        // then
        verify(service, times(1)).update(any(ParcelUpdate.class));
        assertEquals(expectedResponse, response);

        // and status changed to reroute
        assertThat(response.getParcel().getStatus().name()).isEqualTo("REROUTE");
    }

    @Test
    void shouldThrowExceptionWhenParcelIsNull() {
        // given
        final UpdateParcelRequest updateParcelRequest = new UpdateParcelRequest();
        // when, then
        assertThrows(ParcelNotFoundException.class, () -> shipmentReroutePort.reroute(updateParcelRequest));
    }

    @Test
    void shouldThrowExceptionWhenTokenIsNull() {
        // given
        final UpdateParcelRequest updateParcelRequest = new UpdateParcelRequest();
        updateParcelRequest.setToken(null);
        updateParcelRequest.setParcel(mock(Parcel.class));

        // when && then
        assertThrows(RerouteTokenNotFoundException.class, () -> shipmentReroutePort.reroute(updateParcelRequest));
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

    private ShipmentResponse expectedToBeEqualTo(ShipmentResponse response) {
        return response;
    }
}
