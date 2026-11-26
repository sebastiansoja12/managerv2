package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.secondary.ReturningServicePort;
import com.warehouse.shipment.application.port.secondary.ShipmentReadModelRepository;
import com.warehouse.shipment.application.service.RouteLogService;
import com.warehouse.shipment.application.service.ShipmentResultFactory;
import com.warehouse.shipment.domain.exception.ReturnDetailsUnavailableException;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentDeliveryDetails;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShipmentResultFactoryTest {
    private final RouteLogService routeLogService = mock(RouteLogService.class);
    private final ReturningServicePort returningServicePort = mock(ReturningServicePort.class);
    private final ShipmentReadModelRepository shipmentReadModelRepository = mock(ShipmentReadModelRepository.class);
    private final ShipmentResultFactory factory = new ShipmentResultFactory(mock(DepartmentServicePort.class),
            routeLogService, returningServicePort, shipmentReadModelRepository);

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

    @Test
    void shouldCreateResultWithDeliveryDetailsFromReadModel() {
        final Shipment shipment = DataTestCreator.shipment(null, false);
        final ShipmentDeliveryDetails deliveryDetails = new ShipmentDeliveryDetails(new DepartmentId(1L),
                new DepartmentId(2L), new DepartmentId(1L), null, null);
        when(shipmentReadModelRepository.findDeliveryDetails(shipment.getShipmentId()))
                .thenReturn(Optional.of(deliveryDetails));

        final ShipmentResult result = factory.createWithDeliveryDetails(shipment);

        assertEquals(deliveryDetails, result.deliveryDetails());
    }

    @Test
    void shouldCreateResultWithPalletTrackingNumberFromReadModel() {
        final Shipment shipment = DataTestCreator.shipment(null, false);
        final PalletTrackingNumber palletTrackingNumber = new PalletTrackingNumber("PAL000000000001");
        when(shipmentReadModelRepository.findPalletTrackingNumber(shipment.getShipmentId()))
                .thenReturn(Optional.of(palletTrackingNumber));

        final ShipmentResult result = factory.createWithDeliveryDetails(shipment);

        assertEquals(palletTrackingNumber, result.palletTrackingNumber());
    }
}
