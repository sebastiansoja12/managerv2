package com.warehouse.palletmanagement.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.palletmanagement.domain.enumeration.PalletLoadBlockReason;
import com.warehouse.palletmanagement.domain.enumeration.PalletScanErrorCode;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletLoadedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletShipment;
import com.warehouse.palletmanagement.domain.vo.ShipmentLoadResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

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

    public Optional<PalletScanErrorCode> rejectionReason(final Supplier<List<PalletShipment>> shipments) {
        return rejectionReason().or(() -> destinationMismatch(shipments));
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
                this.pallet.shipmentIds(), this.pallet.originDepartmentId(), this.pallet.destinationDepartmentId());
    }

    private Optional<PalletScanErrorCode> destinationMismatch(final Supplier<List<PalletShipment>> shipments) {
        final DepartmentId destinationDepartmentId = this.pallet.destinationDepartmentId();
        if (alreadyLoaded() || destinationDepartmentId == null || this.pallet.attributes().mixedDestinationsAllowed()) {
            return Optional.empty();
        }
        final boolean mismatch = shipments.get().stream()
                .map(PalletShipment::destinationDepartmentId)
                .filter(Objects::nonNull)
                .anyMatch(shipmentDestinationDepartmentId -> !shipmentDestinationDepartmentId
                        .equals(destinationDepartmentId));
        return mismatch ? Optional.of(PalletScanErrorCode.SHIPMENT_DESTINATION_MISMATCH) : Optional.empty();
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
