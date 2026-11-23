package com.warehouse.palletmanagement.application.listener;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.PalletFixture;
import com.warehouse.palletmanagement.api.event.PalletDepartmentsChangedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.PalletLoadedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.PalletLoadingRevertedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.PalletUnloadedIntegrationEvent;
import com.warehouse.palletmanagement.domain.event.PalletDepartmentsChanged;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.event.PalletLoadingReverted;
import com.warehouse.palletmanagement.domain.event.PalletUnloaded;
import com.warehouse.palletmanagement.domain.vo.PalletDepartmentsChangedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletLoadedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletLoadingRevertedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletUnloadedSnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PalletIntegrationEventTranslatorTest {

    private final PalletIntegrationEventTranslator translator = new PalletIntegrationEventTranslator();

    @Test
    void shouldPublishPalletLoadedIntegrationEvent() {
        final PalletLoadedIntegrationEvent event = this.translator.translate(new PalletLoaded(new PalletLoadedSnapshot(PalletFixture.PALLET_ID,
                PalletFixture.TRACKING_NUMBER, List.of(new ShipmentId(1L), new ShipmentId(2L)),
                new DepartmentId(5L), new DepartmentId(6L)), Instant.now()));

        assertThat(event.payload().palletId()).isEqualTo("pallet-1");
        assertThat(event.payload().trackingNumber()).isEqualTo("PAL000000000001");
        assertThat(event.payload().shipmentIds()).containsExactly(1L, 2L);
        assertThat(event.payload().originDepartmentId()).isEqualTo(5L);
        assertThat(event.payload().destinationDepartmentId()).isEqualTo(6L);
        assertThat(event.eventKey()).isEqualTo("pallet-1");
    }

    @Test
    void shouldPublishPalletLoadingRevertedIntegrationEvent() {
        final PalletLoadingRevertedIntegrationEvent event = this.translator.translate(new PalletLoadingReverted(new PalletLoadingRevertedSnapshot(PalletFixture.PALLET_ID,
                PalletFixture.TRACKING_NUMBER, List.of(new ShipmentId(1L))), Instant.now()));

        assertThat(event.payload().palletId()).isEqualTo("pallet-1");
        assertThat(event.payload().shipmentIds()).containsExactly(1L);
        assertThat(event.eventKey()).isEqualTo("pallet-1");
    }

    @Test
    void shouldPublishPalletDepartmentsChangedIntegrationEvent() {
        final PalletDepartmentsChangedIntegrationEvent event = this.translator.translate(new PalletDepartmentsChanged(new PalletDepartmentsChangedSnapshot(PalletFixture.PALLET_ID,
                PalletFixture.TRACKING_NUMBER, new DepartmentId(5L), new DepartmentId(6L)), Instant.now()));

        assertThat(event.payload().palletId()).isEqualTo(PalletFixture.PALLET_ID);
        assertThat(event.payload().originDepartmentId()).isEqualTo(new DepartmentId(5L));
        assertThat(event.payload().destinationDepartmentId()).isEqualTo(new DepartmentId(6L));
        assertThat(event.eventKey()).isEqualTo("pallet-1");
    }

    @Test
    void shouldPublishPalletUnloadedIntegrationEvent() {
        final PalletUnloadedIntegrationEvent event = this.translator.translate(new PalletUnloaded(new PalletUnloadedSnapshot(PalletFixture.PALLET_ID,
                PalletFixture.TRACKING_NUMBER, List.of(new ShipmentId(1L)), List.of(new ShipmentId(2L)),
                new DepartmentId(6L)), Instant.now()));

        assertThat(event.payload().unloadedShipmentIds()).containsExactly(new ShipmentId(1L));
        assertThat(event.payload().missingShipmentIds()).containsExactly(new ShipmentId(2L));
        assertThat(event.payload().unloadDepartmentId()).isEqualTo(new DepartmentId(6L));
        assertThat(event.eventKey()).isEqualTo("pallet-1");
    }
}
