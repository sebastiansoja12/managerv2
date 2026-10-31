package com.warehouse.palletmanagement.application.port.primary.result;

import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.ShipmentLoadResult;

import java.util.List;

public record PalletUnloadResult(PalletDetails pallet,
                                 boolean alreadyUnloaded,
                                 List<ShipmentLoadResult> shipments) {

    public static PalletUnloadResult alreadyUnloaded(final PalletDetails pallet) {
        return new PalletUnloadResult(pallet, true, List.of());
    }

    public static PalletUnloadResult unloaded(final PalletDetails pallet, final List<ShipmentLoadResult> shipments) {
        return new PalletUnloadResult(pallet, false, shipments);
    }

    public boolean success() {
        return shipments.stream().allMatch(ShipmentLoadResult::loaded);
    }
}
