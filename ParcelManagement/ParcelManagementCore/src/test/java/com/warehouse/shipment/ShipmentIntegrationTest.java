package com.warehouse.shipment;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.warehouse.shipment.domain.vo.ShipmentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.warehouse.shipment.configuration.ShipmentTestConfiguration;
import com.warehouse.shipment.domain.exception.ParcelNotFoundException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.vo.ParcelId;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.voronoi.VoronoiService;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DataJpaTest
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
public class ShipmentIntegrationTest {

    @Autowired
    private ShipmentPort shipmentPort;

    @Autowired
    private PathFinderServicePort pathFinderServicePort;

    @Autowired
    private VoronoiService voronoiService;

    @Autowired
    private RouteLogServicePort routeLogServicePort;

    @Test
    void shouldNotShipWhenThereIsNoParcelInRequest() {
        // given
        final ShipmentRequest request = ShipmentRequest.builder()
                .parcel(null)
                .build();
        // when
        final Executable executable = () -> shipmentPort.ship(request);
        // then
        final ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, executable);
        assertEquals(expectedToBe("Parcel not found in request"), exception.getMessage());
    }

    @Test
    void shouldNotShipParcelWhenRouteLogIsNotAvailable() {
        // given
        final ShipmentParcel shipmentParcel = createShipmentParcel();
        final ShipmentRequest request = ShipmentRequest.builder()
                .parcel(shipmentParcel)
                .build();
        final ParcelId parcelId = ParcelId.builder().value(1000000000L).build();
        when(voronoiService.findFastestRoute(shipmentParcel.getDestination())).thenReturn("KT3");
        doThrow(new RuntimeException("Error while registering route for parcel"))
                .when(routeLogServicePort)
                .initializeRouteProcess(parcelId);
        // when
        final Executable executable = () -> shipmentPort.ship(request);
        // then
        final RuntimeException exception = assertThrows(RuntimeException.class, executable);
        assertEquals(expectedToBe("Error while registering route for parcel"), exception.getMessage());
    }

    private ShipmentParcel createShipmentParcel() {
        return ShipmentParcel.builder()
                .parcelSize(Size.TEST)
                .price(99)
                .status(Status.CREATED)
                .sender(createSender())
                .recipient(createRecipient())
                .build();
    }

    private Parcel createParcel() {
        return Parcel.builder()
                .parcelSize(Size.TEST)
                .price(99)
                .parcelStatus(Status.CREATED)
                .sender(createSender())
                .recipient(createRecipient())
                .build();
    }

    private Recipient createRecipient() {
        return Recipient.builder()
                .firstName("test")
                .lastName("test")
                .city("Katowice")
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

    private <T> T expectedToBe(T value) {
        return value;
    }

}
