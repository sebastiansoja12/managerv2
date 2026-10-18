package com.warehouse.palletmanagement.application.listener;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.PalletFixture;
import com.warehouse.palletmanagement.api.event.PalletLoadedIntegrationEvent;
import com.warehouse.palletmanagement.application.port.secondary.PalletIntegrationEventServicePort;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.vo.PalletLoadedSnapshot;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PalletIntegrationEventListenerTest {

    @Test
    void shouldPublishPalletLoadedIntegrationEvent() {
        final PalletIntegrationEventServicePort servicePort = mock(PalletIntegrationEventServicePort.class);
        final PalletIntegrationEventListener listener = new PalletIntegrationEventListener(servicePort);

        listener.handle(new PalletLoaded(new PalletLoadedSnapshot(PalletFixture.PALLET_ID,
                PalletFixture.TRACKING_NUMBER, List.of(new ShipmentId(1L), new ShipmentId(2L))), Instant.now()));

        final ArgumentCaptor<PalletLoadedIntegrationEvent> event =
                ArgumentCaptor.forClass(PalletLoadedIntegrationEvent.class);
        verify(servicePort).publishEvent(event.capture());
        assertThat(event.getValue().payload().palletId()).isEqualTo("pallet-1");
        assertThat(event.getValue().payload().trackingNumber()).isEqualTo("PAL000000000001");
        assertThat(event.getValue().payload().shipmentIds()).containsExactly(1L, 2L);
        assertThat(event.getValue().eventKey()).isEqualTo("pallet-1");
    }
}
