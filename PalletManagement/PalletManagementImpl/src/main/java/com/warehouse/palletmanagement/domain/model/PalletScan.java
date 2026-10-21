package com.warehouse.palletmanagement.domain.model;

import com.warehouse.palletmanagement.domain.enumeration.PalletLoadBlockReason;
import com.warehouse.palletmanagement.domain.enumeration.PalletScanErrorCode;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletLoadedSnapshot;
import com.warehouse.palletmanagement.domain.vo.ShipmentLoadResult;

import java.util.List;
import java.util.Optional;

public class PalletScan {

    private final PalletDetails pallet;

    public PalletScan(final PalletDetails pallet) {
        this.pallet = pallet;
    }

    public PalletDetails pallet() {
        return pallet;
    }

    public boolean alreadyLoaded() {
        return PalletStatus.LOADED.equals(this.pallet.palletStatus());
    }

    public Optional<PalletScanErrorCode> rejectionReason() {
        if (alreadyLoaded()) {
            return Optional.empty();
        }
        if (this.pallet.shipmentIds().isEmpty()) {
            return Optional.of(PalletScanErrorCode.PALLET_EMPTY);
        }
        final Optional<PalletLoadBlockReason> blockReason = this.pallet.blockReason();
        if (blockReason.isPresent()) {
            return blockReason.map(this::map);
        }
        if (!this.pallet.loadable() || !PalletStatus.READY.equals(this.pallet.palletStatus())) {
            return Optional.of(PalletScanErrorCode.PALLET_INVALID_STATUS);
        }
        return Optional.empty();
    }

    public boolean allShipmentsLoaded(final List<ShipmentLoadResult> results) {
        return results.size() == this.pallet.shipmentIds().size()
                && results.stream().allMatch(ShipmentLoadResult::loaded);
    }

    public boolean requiresLoadedNotification(final List<ShipmentLoadResult> results) {
        return !alreadyLoaded() && allShipmentsLoaded(results);
    }

    public PalletLoadedSnapshot loadedSnapshot() {
        return new PalletLoadedSnapshot(this.pallet.palletId(), this.pallet.trackingNumber(),
                this.pallet.shipmentIds());
    }

    private PalletScanErrorCode map(final PalletLoadBlockReason reason) {
        return switch (reason) {
            case EMPTY -> PalletScanErrorCode.PALLET_EMPTY;
            case LOCKED -> PalletScanErrorCode.PALLET_LOCKED;
            case OVERWEIGHT -> PalletScanErrorCode.PALLET_OVERWEIGHT;
            case INVALID_STATUS -> PalletScanErrorCode.PALLET_INVALID_STATUS;
        };
    }
}
