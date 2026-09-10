package com.warehouse.logisticsdelivery.infrastructure.adapter.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryReadModelPort;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeliveryReadModelRebuildApiServiceAdapterTest {

    @Test
    void shouldRebuildReadModelsForOperator() {
        final DeliveryReadModelPort deliveryReadModelPort = mock(DeliveryReadModelPort.class);
        final DeliveryReadModelRebuildApiServiceAdapter adapter =
                new DeliveryReadModelRebuildApiServiceAdapter(deliveryReadModelPort, new OperatorContext());
        final LocalDate dateFrom = LocalDate.of(2026, 10, 1);
        final LocalDate dateTo = LocalDate.of(2026, 10, 20);
        when(deliveryReadModelPort.syncReadModels(dateFrom, dateTo)).thenReturn(3);

        final int rebuilt = adapter.rebuildReadModels(OperatorId.of(7L), dateFrom, dateTo);

        assertThat(rebuilt).isEqualTo(3);
        verify(deliveryReadModelPort).syncReadModels(dateFrom, dateTo);
    }
}
