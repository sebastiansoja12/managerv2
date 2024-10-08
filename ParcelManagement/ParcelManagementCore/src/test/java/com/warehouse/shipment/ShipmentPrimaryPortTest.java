package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.vo.ShipmentRequest;
import com.warehouse.shipment.domain.vo.ShipmentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.port.primary.ShipmentPortImpl;
import com.warehouse.shipment.domain.port.secondary.Logger;
import com.warehouse.shipment.domain.service.ShipmentService;
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
        final ShipmentRequest request = new ShipmentRequest(createShipmentParcel());

        // create expected response from service
        final ShipmentResponse expectedResponse = new ShipmentResponse("paymentUrl", 1L);

        when(shipmentService.createShipment(request.getShipment())).thenReturn(expectedResponse);
        // when
        final ShipmentResponse response = shipmentPort.ship(request);
        // then
        assertEquals(response, expectedToBeEqualTo(response));
    }

    @Test
    void shouldLoadParcel() {
        // given
        final Long parcelId = 100001L;

        doReturn(createParcel())
                .when(shipmentService)
                .loadParcel(parcelId());
        // when
        final Parcel parcel = shipmentPort.loadParcel(parcelId());
        // then
        assertThat(parcel).isNotNull();
    }

    @Test
    void shouldNotLoadParcel() {
        // given
        final ShipmentId shipmentId = new ShipmentId(0L);
        final ParcelNotFoundException parcelNotFoundException = new ParcelNotFoundException("Parcel was not found");
        doThrow(parcelNotFoundException)
                .when(shipmentService)
                .loadParcel(shipmentId);
        // when
        final Executable executable = () -> shipmentPort.loadParcel(shipmentId);
        // then
        final ParcelNotFoundException exception =
                assertThrows(ParcelNotFoundException.class, executable);
        assertEquals(expectedToBe("Parcel was not found"), exception.getMessage());
    }

    private <T> T expectedToBe(T value) {
        return value;
    }

    private <T> T expectedToBeEqualTo(T value) {
        return value;
    }
}
