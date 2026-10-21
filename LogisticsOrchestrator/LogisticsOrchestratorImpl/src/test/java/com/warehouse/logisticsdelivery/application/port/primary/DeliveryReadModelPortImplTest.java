package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryReadModelRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.warehouse.logisticsdelivery.DeliveryFixture.shipmentDelivery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeliveryReadModelPortImplTest {

    private DeliveryRepository deliveryRepository;
    private DeliveryReadModelRepository deliveryReadModelRepository;
    private DeliveryReadModelPortImpl deliveryReadModelPort;

    @BeforeEach
    void setUp() {
        this.deliveryRepository = mock(DeliveryRepository.class);
        this.deliveryReadModelRepository = mock(DeliveryReadModelRepository.class);
        this.deliveryReadModelPort = new DeliveryReadModelPortImpl(this.deliveryRepository,
                this.deliveryReadModelRepository);
    }

    @Test
    void shouldRebuildReadModelForDeliveriesCreatedInDateRange() {
        final Delivery firstDelivery = shipmentDelivery(new ShipmentId(10L));
        final Delivery secondDelivery = shipmentDelivery(new ShipmentId(11L));
        when(this.deliveryRepository.findAllCreatedBetween(LocalDateTime.of(2026, 10, 1, 0, 0),
                LocalDateTime.of(2026, 10, 21, 0, 0))).thenReturn(List.of(firstDelivery, secondDelivery));

        final int rebuilt = this.deliveryReadModelPort.syncReadModels(LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 20));

        assertThat(rebuilt).isEqualTo(2);
        verify(this.deliveryReadModelRepository).sync(firstDelivery.snapshot());
        verify(this.deliveryReadModelRepository).sync(secondDelivery.snapshot());
    }
}
