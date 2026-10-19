package com.warehouse.logistics.domain;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.logistics.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.DeliveryType;
import com.warehouse.logistics.domain.model.CreatePalletDeliveryCommand;
import com.warehouse.logistics.domain.model.Delivery;
import com.warehouse.logistics.domain.model.DeliveryTarget;
import com.warehouse.logistics.domain.model.DeliveryTargetType;
import com.warehouse.logistics.domain.port.primary.LogisticsPortImpl;
import com.warehouse.logistics.domain.service.LogisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PalletDeliveryTest {

    private static final UserId USER_ID = new UserId(1L);

    private LogisticsService logisticsService;
    private LogisticsPortImpl logisticsPort;

    @BeforeEach
    void setUp() {
        this.logisticsService = mock(LogisticsService.class);
        this.logisticsPort = new LogisticsPortImpl(this.logisticsService);
    }

    @Test
    void shouldCreatePalletDeliveryAndReplaceShipmentDeliveries() {
        final Delivery shipmentDelivery = shipmentDelivery(new ShipmentId(10L));
        when(this.logisticsService.findByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.empty());
        when(this.logisticsService.findByTargetAndType(DeliveryTarget.shipment(new ShipmentId(10L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(shipmentDelivery));
        when(this.logisticsService.findByTargetAndType(DeliveryTarget.shipment(new ShipmentId(11L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.empty());

        this.logisticsPort.createPalletDelivery(new CreatePalletDeliveryCommand("pallet-1",
                List.of(new ShipmentId(10L), new ShipmentId(11L)), USER_ID));

        final ArgumentCaptor<Delivery> saved = ArgumentCaptor.forClass(Delivery.class);
        verify(this.logisticsService, times(2)).createOrUpdate(saved.capture());
        final Delivery palletDelivery = saved.getAllValues().get(0);
        assertThat(palletDelivery.getTarget().type()).isEqualTo(DeliveryTargetType.PALLET);
        assertThat(palletDelivery.getTarget().id()).isEqualTo("pallet-1");
        assertThat(palletDelivery.getShipmentId()).isNull();
        assertThat(saved.getAllValues().get(1)).isSameAs(shipmentDelivery);
        assertThat(shipmentDelivery.getStatus()).isEqualTo(DeliveryLifecycleStatus.CANCELED);
        assertThat(shipmentDelivery.getDeliverySteps()).hasSize(2);
    }

    @Test
    void shouldNotDuplicatePalletDeliveryOnRepeatedEvent() {
        final Delivery existing = Delivery.forPallet(DeliveryTarget.pallet("pallet-1"), USER_ID);
        final Delivery replaced = shipmentDelivery(new ShipmentId(10L));
        replaced.markAsCanceled();
        when(this.logisticsService.findByTargetAndType(DeliveryTarget.pallet("pallet-1"), DeliveryType.OUTBOUND))
                .thenReturn(Optional.of(existing));
        when(this.logisticsService.findByTargetAndType(DeliveryTarget.shipment(new ShipmentId(10L)),
                DeliveryType.OUTBOUND)).thenReturn(Optional.of(replaced));

        this.logisticsPort.createPalletDelivery(new CreatePalletDeliveryCommand("pallet-1",
                List.of(new ShipmentId(10L)), USER_ID));

        verify(this.logisticsService, never()).createOrUpdate(any());
    }

    @Test
    void shouldIgnoreCompletionOfReplacedShipmentDelivery() {
        final Delivery replaced = shipmentDelivery(new ShipmentId(10L));
        replaced.replaceByPalletDelivery(Delivery.forPallet(DeliveryTarget.pallet("p"), USER_ID).getDeliveryId(),
                USER_ID);

        assertThat(replaced.isActive()).isFalse();
        assertThat(replaced.replaceByPalletDelivery(replaced.getDeliveryId(), USER_ID)).isFalse();
    }

    private Delivery shipmentDelivery(final ShipmentId shipmentId) {
        return new Delivery(DeliveryTarget.shipment(shipmentId), shipmentId, DeliveryType.OUTBOUND,
                null, null, null, DeliveryMethod.COURIER, null, null, null, false, USER_ID);
    }
}
