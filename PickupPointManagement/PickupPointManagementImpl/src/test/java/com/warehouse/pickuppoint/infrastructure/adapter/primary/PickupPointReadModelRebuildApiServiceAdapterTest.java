package com.warehouse.pickuppoint.infrastructure.adapter.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.pickuppoint.application.port.primary.PickupPointReadModelSyncPort;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PickupPointReadModelRebuildApiServiceAdapterTest {

    @Test
    void shouldRebuildReadModelsForOperator() {
        final PickupPointReadModelSyncPort pickupPointReadModelSyncPort = mock(PickupPointReadModelSyncPort.class);
        final PickupPointReadModelRebuildApiServiceAdapter adapter =
                new PickupPointReadModelRebuildApiServiceAdapter(pickupPointReadModelSyncPort, new OperatorContext());
        final LocalDate dateFrom = LocalDate.of(2026, 10, 1);
        final LocalDate dateTo = LocalDate.of(2026, 10, 20);
        when(pickupPointReadModelSyncPort.syncReadModels(dateFrom, dateTo)).thenReturn(4);

        final int rebuilt = adapter.rebuildReadModels(OperatorId.of(7L), dateFrom, dateTo);

        assertThat(rebuilt).isEqualTo(4);
        verify(pickupPointReadModelSyncPort).syncReadModels(dateFrom, dateTo);
    }
}
