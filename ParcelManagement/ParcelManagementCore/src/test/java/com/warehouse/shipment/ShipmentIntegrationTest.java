package com.warehouse.shipment;


import static com.warehouse.shipment.DataTestCreator.createShipmentParcel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.warehouse.commonassets.identificator.ShipmentId;
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
import com.warehouse.shipment.domain.exception.ShipmentEmptyRequestException;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
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
        final ShipmentRequest request = new ShipmentRequest(null);
        // when
        final Executable executable = () -> shipmentPort.ship(request);
        // then
        final ShipmentEmptyRequestException exception = assertThrows(ShipmentEmptyRequestException.class, executable);
        assertEquals(expectedToBe("Parcel not found in request"), exception.getMessage());
    }

    @Test
    void shouldNotShipParcelWhenRouteLogIsNotAvailable() {
        // given
        final Shipment shipment = createShipmentParcel();
        final ShipmentRequest request = new ShipmentRequest(shipment);
        final ShipmentId shipmentId = DataTestCreator.shipmentId();
        when(voronoiService.findFastestRoute(shipment.getDestination())).thenReturn("KT3");
        doThrow(new RuntimeException("Error while registering route for parcel"))
                .when(routeLogServicePort)
                .initializeRouteProcess(shipmentId);
        // when
        final Executable executable = () -> shipmentPort.ship(request);
        // then
        final RuntimeException exception = assertThrows(RuntimeException.class, executable);
        assertEquals(expectedToBe("Error while registering route for parcel"), exception.getMessage());
    }

    private <T> T expectedToBe(T value) {
        return value;
    }

}
