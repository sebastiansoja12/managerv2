package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.api.ReturningApiService;
import com.warehouse.returning.api.dto.ReturnDetailsDto;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class ReturningServiceClientTest {
    @Test
    void shouldReadReturnThroughReturningApi() {
        final ReturningApiService api = mock(ReturningApiService.class);
        final ReturningServiceClient client = new ReturningServiceClient(api);
        final ShipmentId shipmentId = new ShipmentId(456L);
        final Optional<ReturnDetailsDto> response = Optional.of(new ReturnDetailsDto(
                new com.warehouse.returning.api.dto.LongValueDto(123L),
                new com.warehouse.returning.api.dto.LongValueDto(456L), "Damaged",
                com.warehouse.commonassets.enumeration.ReturnStatus.CREATED,
                null, null, null, null, null, null, null, null, null, null, null));
        when(api.findByShipmentId(shipmentId)).thenReturn(response);

        assertSame(response, client.findReturnByShipmentId(shipmentId));
        verify(api).findByShipmentId(shipmentId);
    }
}
