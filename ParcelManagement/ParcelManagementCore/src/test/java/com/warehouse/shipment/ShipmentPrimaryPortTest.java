package com.warehouse.shipment;

import com.warehouse.shipment.domain.enumeration.Size;
import com.warehouse.shipment.domain.enumeration.Status;
import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.exception.RerouteTokenNotFoundException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.service.ShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShipmentPrimaryPortTest {

    @Mock
    private ShipmentService shipmentService;

    private ShipmentPort shipmentPort;

    private final Integer VALID_TOKEN = 12345;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shipmentPort = new ShipmentPortImpl(shipmentService);
    }

    @Test
    void shouldShip() {
        // given
        final ShipmentRequest shipmentRequest = new ShipmentRequest();

        final ShipmentResponse expectedResponse = new ShipmentResponse();
        when(shipmentService.ship(any(ShipmentRequest.class))).thenReturn(expectedResponse);

        // when
        final ShipmentResponse response = shipmentPort.ship(shipmentRequest);

        // then
        assertEquals(expectedResponse, response);
        verify(shipmentService, times(1)).ship(shipmentRequest);
    }

    @Test
    void shouldDeleteParcel() {
        // given
        final Long parcelId = 1L;

        // when
        shipmentPort.delete(parcelId);

        // then
        verify(shipmentService, times(1)).delete(parcelId);
    }

    @Test
    void shouldLoadParcel() {
        // given
        final Long parcelId = 1L;

        final Parcel expectedParcel = new Parcel();
        when(shipmentService.loadParcel(parcelId)).thenReturn(expectedParcel);

        // when
        final Parcel parcel = shipmentPort.loadParcel(parcelId);

        // then
        verify(shipmentService, times(1)).loadParcel(parcelId);
        assertEquals(expectedParcel, parcel);

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

        when(shipmentService.update(any(ParcelUpdate.class))).thenReturn(expectedResponse);
        // when
        final UpdateParcelResponse response = shipmentPort.update(updateParcelRequest);

        // then
        verify(shipmentService, times(1)).update(any(ParcelUpdate.class));
        assertEquals(expectedResponse, response);

        // and status changed to reroute
        assertThat(response.getParcel().getStatus().name()).isEqualTo("REROUTE");
    }

    @Test
    void shouldThrowExceptionWhenParcelIsNull() {
        // given
        final String errorMessage = "Parcel is null";
        final UpdateParcelRequest updateParcelRequest = new UpdateParcelRequest();

        doThrow(new ParcelNotFoundException(errorMessage)).when(shipmentService).update(any(ParcelUpdate.class));

        // when, then
        assertThrows(ParcelNotFoundException.class, () -> shipmentPort.update(updateParcelRequest));
    }

    @Test
    void shouldThrowExceptionWhenTokenIsNull() {
        // given
        final String errorMessage = "Reroute token is null";
        final UpdateParcelRequest updateParcelRequest = new UpdateParcelRequest();
        updateParcelRequest.setToken(null);
        updateParcelRequest.setParcel(mock(Parcel.class));

        doThrow(new RerouteTokenNotFoundException(errorMessage)).when(shipmentService)
                .update(any(ParcelUpdate.class));

        // when && then
        assertThrows(RerouteTokenNotFoundException.class, () -> shipmentPort.update(updateParcelRequest));
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
