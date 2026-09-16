package com.warehouse.shipment;

import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.shipment.application.port.primary.ShipmentQueryPort;
import com.warehouse.shipment.application.port.primary.ShipmentQueryPortImpl;
import com.warehouse.shipment.application.port.primary.result.ShipmentPageResult;
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

    private final SpecificationRepository<ShipmentSearchCriteria, Shipment> repository = mock(SpecificationRepository.class);
    private final ShipmentResultFactory factory = new ShipmentResultFactory(mock(DepartmentServicePort.class),
            mock(RouteLogService.class), mock(ReturningServicePort.class));
    private final ShipmentQueryPort queryPort = new ShipmentQueryPortImpl(repository, factory);

    @Test
    void shouldSearchShipmentsAndMapResults() {
        final ShipmentSearchCriteria criteria = criteria(0, 50);
        final Shipment shipment = DataTestCreator.shipment();
        when(repository.list(criteria)).thenReturn(List.of(shipment));
        when(repository.count(criteria)).thenReturn(1L);

        final ShipmentPageResult result = queryPort.searchShipments(criteria);

        assertEquals(1, result.content().size());
        assertEquals(shipment.snapshot(), result.content().getFirst().snapshot());
        verify(repository).list(criteria);
    }

    @Test
    void shouldReturnPageMetadata() {
        final ShipmentSearchCriteria criteria = criteria(2, 20);
        when(repository.list(criteria)).thenReturn(List.of());
        when(repository.count(criteria)).thenReturn(41L);

        final ShipmentPageResult result = queryPort.searchShipments(criteria);

        assertEquals(2, result.page());
        assertEquals(20, result.size());
        assertEquals(41L, result.totalElements());
        assertEquals(3, result.totalPages());
    }

    @Test
    void shouldReturnNoPagesWhenNothingFound() {
        final ShipmentSearchCriteria criteria = criteria(null, null);
        when(repository.list(criteria)).thenReturn(List.of());
        when(repository.count(criteria)).thenReturn(0L);

        final ShipmentPageResult result = queryPort.searchShipments(criteria);

        assertEquals(0, result.page());
        assertEquals(50, result.size());
        assertEquals(0L, result.totalElements());
        assertEquals(0, result.totalPages());
    }

    private ShipmentSearchCriteria criteria(final Integer page, final Integer size) {
        return new ShipmentSearchCriteria(null, null, List.of(), List.of(), null, null,
                null, null, null, null, null, null, page, size);
    }
}
