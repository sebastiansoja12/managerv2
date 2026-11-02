package com.warehouse.logisticsdelivery.infrastructure.adapter.primary;

import com.warehouse.logisticsdelivery.application.port.primary.DeliveryReadModelPort;
import com.warehouse.logisticsdelivery.application.port.primary.command.SearchDeliveriesCommand;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadPage;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DeliveryReadModelResponseMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeliveryQueryControllerTest {

    private final DeliveryReadModelPort deliveryReadModelPort = mock(DeliveryReadModelPort.class);

    private final DeliveryQueryController controller =
            new DeliveryQueryController(deliveryReadModelPort, new DeliveryReadModelResponseMapper());

    @Test
    void shouldPassMissingDestinationFilterToReadModel() {
        when(deliveryReadModelPort.search(any())).thenReturn(new DeliveryReadPage(List.of(), 0, 25, false));

        controller.query(0, 25, null, null, null, true, true, null, null, null, null, null, null, true, null, null,
                "CREATED", "desc");

        final ArgumentCaptor<SearchDeliveriesCommand> command = ArgumentCaptor.forClass(SearchDeliveriesCommand.class);
        verify(deliveryReadModelPort).search(command.capture());
        assertThat(command.getValue().filter().missingDestination()).isTrue();
        assertThat(command.getValue().filter().hideCanceled()).isTrue();
        assertThat(command.getValue().filter().hideCompleted()).isTrue();
    }
}
