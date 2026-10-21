package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.secondary.SourceShipment;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryDerivationType;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.dto.SourceShipmentDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShipmentServiceAdapterTest {

    private final ShipmentApiService shipmentApiService = mock(ShipmentApiService.class);
    private final ShipmentServiceAdapter adapter = new ShipmentServiceAdapter(shipmentApiService);

    @ParameterizedTest
    @CsvSource({"RETURN, RETURN", "REDIRECT, REDIRECT", "CREATED, SPLIT"})
    void shouldMapSourceShipmentStatusToDerivationType(final ShipmentStatus sourceStatus,
                                                       final DeliveryDerivationType derivationType) {
        when(shipmentApiService.findSourceShipment(new ShipmentId(11L)))
                .thenReturn(Optional.of(new SourceShipmentDto(new ShipmentId(10L), sourceStatus)));

        final Optional<SourceShipment> sourceShipment = adapter.findSourceShipment(new ShipmentId(11L));

        assertThat(sourceShipment).contains(new SourceShipment(new ShipmentId(10L), derivationType));
    }

    @Test
    void shouldReturnEmptyWhenShipmentHasNoSourceShipment() {
        when(shipmentApiService.findSourceShipment(new ShipmentId(11L))).thenReturn(Optional.empty());

        assertThat(adapter.findSourceShipment(new ShipmentId(11L))).isEmpty();
    }
}
