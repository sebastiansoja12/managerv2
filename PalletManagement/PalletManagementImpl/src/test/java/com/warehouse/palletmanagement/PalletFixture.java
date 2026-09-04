package com.warehouse.palletmanagement;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.domain.enumeration.PalletLoadBlockReason;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public final class PalletFixture {

    public static final PalletId PALLET_ID = new PalletId("pallet-1");
    public static final PalletTrackingNumber TRACKING_NUMBER = new PalletTrackingNumber("PAL000000000001");

    private PalletFixture() {
    }

    public static PalletDetails readyPallet(final Long... shipmentIds) {
        return pallet(PalletStatus.READY, true, null, shipmentIds);
    }

    public static PalletDetails loadedPallet(final Long... shipmentIds) {
        return pallet(PalletStatus.LOADED, false, null, shipmentIds);
    }

    public static PalletDetails blockedPallet(final PalletStatus status, final PalletLoadBlockReason reason,
                                              final Long... shipmentIds) {
        return pallet(status, false, reason, shipmentIds);
    }

    public static PalletDetails pallet(final PalletStatus status, final boolean loadable,
                                       final PalletLoadBlockReason reason, final Long... shipmentIds) {
        final List<ShipmentId> ids = Arrays.stream(shipmentIds).map(ShipmentId::new).toList();
        return new PalletDetails(PALLET_ID, TRACKING_NUMBER, status, "UNLOCKED", ids, ids.isEmpty(), loadable,
                reason, null, null, null, null, null, Instant.parse("2026-10-17T10:00:00Z"), Instant.parse("2026-10-17T10:00:00Z"), null, null);
    }
}
