package com.warehouse.palletmanagement.application.listener;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.PalletFixture;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.event.PalletLoadingReverted;
import com.warehouse.palletmanagement.domain.event.PalletUnloaded;
import com.warehouse.palletmanagement.domain.vo.PalletLoadedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletLoadingRevertedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletUnloadedSnapshot;
import com.warehouse.shipment.api.event.ShipmentPalletReadModelChanged;
import com.warehouse.shipment.api.event.snapshot.ShipmentPalletReadModelData;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentPalletReadModelTranslatorTest {

    private final ShipmentPalletReadModelTranslator translator = new ShipmentPalletReadModelTranslator();

    @Test
    void shouldPublishPalletTrackingNumberForEveryLoadedShipment() {
        final List<ShipmentPalletReadModelChanged> events = this.translator.translate(new PalletLoaded(new PalletLoadedSnapshot(PalletFixture.PALLET_ID,
                PalletFixture.TRACKING_NUMBER, List.of(new ShipmentId(1L), new ShipmentId(2L)),
                new DepartmentId(5L), new DepartmentId(6L)), Instant.now()));

        assertThat(events).extracting(ShipmentPalletReadModelChanged::payload).containsExactly(
                new ShipmentPalletReadModelData(new ShipmentId(1L), PalletFixture.TRACKING_NUMBER),
                new ShipmentPalletReadModelData(new ShipmentId(2L), PalletFixture.TRACKING_NUMBER));
        assertThat(events).extracting(ShipmentPalletReadModelChanged::eventKey)
                .containsExactly("1", "2");
    }

    @Test
    void shouldClearPalletTrackingNumberWhenLoadingIsReverted() {
        final List<ShipmentPalletReadModelChanged> events = this.translator.translate(new PalletLoadingReverted(new PalletLoadingRevertedSnapshot(PalletFixture.PALLET_ID,
                PalletFixture.TRACKING_NUMBER, List.of(new ShipmentId(1L))), Instant.now()));

        assertThat(events).extracting(ShipmentPalletReadModelChanged::payload).containsExactly(new ShipmentPalletReadModelData(new ShipmentId(1L), null));
    }

    @Test
    void shouldClearPalletTrackingNumberOnlyForUnloadedShipments() {
        final List<ShipmentPalletReadModelChanged> events = this.translator.translate(new PalletUnloaded(new PalletUnloadedSnapshot(PalletFixture.PALLET_ID,
                PalletFixture.TRACKING_NUMBER, List.of(new ShipmentId(1L)), List.of(new ShipmentId(2L)),
                new DepartmentId(6L)), Instant.now()));

        assertThat(events).extracting(ShipmentPalletReadModelChanged::payload).containsExactly(new ShipmentPalletReadModelData(new ShipmentId(1L), null));
    }
}
