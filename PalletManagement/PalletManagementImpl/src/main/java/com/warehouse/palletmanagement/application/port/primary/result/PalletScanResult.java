package com.warehouse.palletmanagement.application.port.primary.result;

import com.warehouse.palletmanagement.domain.enumeration.PalletScanErrorCode;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.ShipmentLoadResult;

import java.util.List;

public record PalletScanResult(PalletDetails pallet,
                               boolean success,
                               boolean alreadyLoaded,
                               PalletScanErrorCode errorCode,
                               List<ShipmentLoadResult> shipments) {

    public static PalletScanResult rejected(final PalletDetails pallet, final PalletScanErrorCode errorCode) {
        return new PalletScanResult(pallet, false, false, errorCode, List.of());
    }

    public static PalletScanResult shipmentsFailed(final PalletDetails pallet, final boolean alreadyLoaded,
                                                   final List<ShipmentLoadResult> shipments) {
        return new PalletScanResult(pallet, false, alreadyLoaded, PalletScanErrorCode.SHIPMENT_STATUS_UPDATE_FAILED,
                shipments);
    }

    public static PalletScanResult loaded(final PalletDetails pallet, final boolean alreadyLoaded,
                                          final List<ShipmentLoadResult> shipments) {
        return new PalletScanResult(pallet, true, alreadyLoaded, null, shipments);
    }
}
