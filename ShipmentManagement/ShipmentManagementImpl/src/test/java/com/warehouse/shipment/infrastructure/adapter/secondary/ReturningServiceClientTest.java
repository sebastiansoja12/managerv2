package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.api.ReturningApiService;
import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.exception.ReturnDetailsUnavailableException;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.ResourceAccessException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReturningServiceClientTest {
    @Test
    void shouldReadReturnThroughReturningApi() {
        final ReturningApiService api = mock(ReturningApiService.class);
        final ReturningServiceClient client = new ReturningServiceClient(api);
        final ShipmentId shipmentId = new ShipmentId(456L);
        final Optional<ReturnDetailsDto> response = Optional.of(new ReturnDetailsDto(
                new com.warehouse.returning.api.dto.LongValueDto(123L),
                new com.warehouse.returning.api.dto.LongValueDto(456L), null, null, null, "Damaged",
                com.warehouse.commonassets.enumeration.ReturnStatus.CREATED,
                null, null, null, null, null, null, null, null, null, null, null));
        when(api.findByShipmentId(shipmentId)).thenReturn(response);

        assertSame(response, client.findReturnByShipmentId(shipmentId));
        verify(api).findByShipmentId(shipmentId);
    }

    @Test
    void shouldReportReturnDetailsUnavailableWhenReturningServiceCannotBeReached() {
        final ReturningApiService api = mock(ReturningApiService.class);
        final ReturningServiceClient client = new ReturningServiceClient(api);
        final ShipmentId shipmentId = new ShipmentId(456L);
        when(api.findByShipmentId(shipmentId)).thenThrow(new ResourceAccessException("Connection refused"));

        assertThrows(ReturnDetailsUnavailableException.class, () -> client.findReturnByShipmentId(shipmentId));
    }

    @Test
    void shouldReportReturnDetailsUnavailableWhenReturningServiceRejectsRequest() {
        final ReturningApiService api = mock(ReturningApiService.class);
        final ReturningServiceClient client = new ReturningServiceClient(api);
        final ShipmentId shipmentId = new ShipmentId(456L);
        when(api.findByShipmentId(shipmentId)).thenThrow(new RestException(503, "RTM rejected the return operation"));

        assertThrows(ReturnDetailsUnavailableException.class, () -> client.findReturnByShipmentId(shipmentId));
    }
}
