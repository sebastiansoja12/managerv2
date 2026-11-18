package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import com.warehouse.shipment.application.port.secondary.ReturningServicePort;
import com.warehouse.shipment.application.service.RouteLogService;
import com.warehouse.shipment.application.service.ShipmentResultFactory;
import com.warehouse.shipment.domain.exception.ReturnDetailsUnavailableException;
import com.warehouse.shipment.domain.model.Shipment;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShipmentResultFactoryTest {
    private final RouteLogService routeLogService = mock(RouteLogService.class);
    private final ReturningServicePort returningServicePort = mock(ReturningServicePort.class);
    private final ShipmentResultFactory factory = new ShipmentResultFactory(mock(DepartmentServicePort.class),
            routeLogService, returningServicePort);

    @Test
    void shouldLoadShipmentWhenReturnDetailsAreUnavailable() {
        final Shipment shipment = DataTestCreator.shipment(null, false);
        when(routeLogService.findByShipmentId(any(ShipmentId.class))).thenReturn(Optional.empty());
        when(returningServicePort.findReturnByShipmentId(shipment.getShipmentId()))
                .thenThrow(new ReturnDetailsUnavailableException("Return details are unavailable", null));

        final ShipmentRouteLog controlCenter = factory.createControlCenter(shipment);

        assertAll(
                () -> assertEquals(shipment.getShipmentId(), controlCenter.shipment().snapshot().shipmentId()),
                () -> assertNull(controlCenter.returnPackage()),
                () -> assertTrue(controlCenter.returnPackageUnavailable())
        );
    }

    @Test
    void shouldMarkReturnDetailsAvailableWhenShipmentHasNoReturn() {
        final Shipment shipment = DataTestCreator.shipment(null, false);
        when(routeLogService.findByShipmentId(any(ShipmentId.class))).thenReturn(Optional.empty());
        when(returningServicePort.findReturnByShipmentId(shipment.getShipmentId())).thenReturn(Optional.empty());

        final ShipmentRouteLog controlCenter = factory.createControlCenter(shipment);

        assertAll(
                () -> assertNull(controlCenter.returnPackage()),
                () -> assertFalse(controlCenter.returnPackageUnavailable())
        );
    }
}
