package com.warehouse.shipment;

import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.shipment.application.port.primary.ShipmentQueryPort;
import com.warehouse.shipment.application.port.primary.ShipmentQueryPortImpl;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import com.warehouse.shipment.application.port.secondary.ReturningServicePort;
import com.warehouse.shipment.application.service.RouteLogService;
import com.warehouse.shipment.application.service.ShipmentResultFactory;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ShipmentQueryPortImplTest {
    @Test
    void shouldSearchShipmentsAndMapResults() {
        final SpecificationRepository<ShipmentSearchCriteria, Shipment> repository = mock(SpecificationRepository.class);
        final ShipmentResultFactory factory = new ShipmentResultFactory(mock(DepartmentServicePort.class),
                mock(RouteLogService.class), mock(ReturningServicePort.class));
        final ShipmentQueryPort queryPort = new ShipmentQueryPortImpl(repository, factory);
        final ShipmentSearchCriteria criteria = mock(ShipmentSearchCriteria.class);
        final Shipment shipment = DataTestCreator.shipment();
        when(repository.list(criteria)).thenReturn(List.of(shipment));

        final List<ShipmentResult> results = queryPort.searchShipments(criteria);

        assertEquals(1, results.size());
        assertEquals(shipment.snapshot(), results.getFirst().snapshot());
        verify(repository).list(criteria);
    }
}
