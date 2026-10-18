package com.warehouse.palletmanagement.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.PalletFixture;
import com.warehouse.palletmanagement.domain.enumeration.PalletLoadBlockReason;
import com.warehouse.palletmanagement.domain.enumeration.PalletScanErrorCode;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.vo.ShipmentLoadResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PalletScanTest {

    @Test
    void shouldAcceptReadyPallet() {
        final PalletScan palletScan = new PalletScan(PalletFixture.readyPallet(1L, 2L));

        assertThat(palletScan.rejectionReason()).isEmpty();
        assertThat(palletScan.alreadyLoaded()).isFalse();
    }

    @Test
    void shouldAcceptRescanOfLoadedPallet() {
        final PalletScan palletScan = new PalletScan(PalletFixture.loadedPallet(1L));

        assertThat(palletScan.rejectionReason()).isEmpty();
        assertThat(palletScan.alreadyLoaded()).isTrue();
        assertThat(palletScan.requiresLoadedNotification(
                List.of(ShipmentLoadResult.loaded(new ShipmentId(1L), "SENT")))).isFalse();
    }

    @Test
    void shouldRejectEmptyPallet() {
        final PalletScan palletScan = new PalletScan(
                PalletFixture.blockedPallet(PalletStatus.CREATED, PalletLoadBlockReason.EMPTY));

        assertThat(palletScan.rejectionReason()).contains(PalletScanErrorCode.PALLET_EMPTY);
    }

    @Test
    void shouldRejectLockedPallet() {
        final PalletScan palletScan = new PalletScan(
                PalletFixture.blockedPallet(PalletStatus.READY, PalletLoadBlockReason.LOCKED, 1L));

        assertThat(palletScan.rejectionReason()).contains(PalletScanErrorCode.PALLET_LOCKED);
    }

    @Test
    void shouldRejectOverweightPallet() {
        final PalletScan palletScan = new PalletScan(
                PalletFixture.blockedPallet(PalletStatus.NOT_READY, PalletLoadBlockReason.OVERWEIGHT, 1L));

        assertThat(palletScan.rejectionReason()).contains(PalletScanErrorCode.PALLET_OVERWEIGHT);
    }

    @Test
    void shouldRejectCancelledPallet() {
        final PalletScan palletScan = new PalletScan(
                PalletFixture.blockedPallet(PalletStatus.CANCELLED, PalletLoadBlockReason.INVALID_STATUS, 1L));

        assertThat(palletScan.rejectionReason()).contains(PalletScanErrorCode.PALLET_INVALID_STATUS);
    }

    @Test
    void shouldRequireAllShipmentsLoaded() {
        final PalletScan palletScan = new PalletScan(PalletFixture.readyPallet(1L, 2L));
        final List<ShipmentLoadResult> partial = List.of(
                ShipmentLoadResult.loaded(new ShipmentId(1L), "SENT"),
                ShipmentLoadResult.failed(new ShipmentId(2L), "Delivered shipment cannot be changed"));
        final List<ShipmentLoadResult> complete = List.of(
                ShipmentLoadResult.loaded(new ShipmentId(1L), "SENT"),
                ShipmentLoadResult.loaded(new ShipmentId(2L), "SENT"));

        assertThat(palletScan.allShipmentsLoaded(partial)).isFalse();
        assertThat(palletScan.allShipmentsLoaded(complete)).isTrue();
        assertThat(palletScan.requiresLoadedNotification(complete)).isTrue();
        assertThat(palletScan.loadedSnapshot().shipmentIds())
                .containsExactly(new ShipmentId(1L), new ShipmentId(2L));
    }
}
