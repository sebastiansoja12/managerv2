package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.domain.enumeration.PalletLoadBlockReason;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public record PalletDetails(PalletId palletId,
                            PalletTrackingNumber trackingNumber,
                            PalletStatus palletStatus,
                            String storageStatus,
                            List<ShipmentId> shipmentIds,
                            boolean empty,
                            boolean loadable,
                            PalletLoadBlockReason loadBlockReason,
                            PalletWeight grossWeight,
                            PalletWeight maxGrossWeight,
                            DepartmentId originDepartmentId,
                            DepartmentId destinationDepartmentId,
                            String sealNumber,
                            Instant created,
                            Instant modified,
                            PalletAttributes attributes,
                            PalletCarrier carrier) {

    public PalletDetails {
        shipmentIds = shipmentIds == null ? List.of() : List.copyOf(shipmentIds);
        attributes = attributes == null ? PalletAttributes.DEFAULT : attributes;
    }

    public PalletDetails withCarrier(final PalletCarrier carrier) {
        return new PalletDetails(palletId, trackingNumber, palletStatus, storageStatus, shipmentIds, empty, loadable,
                loadBlockReason, grossWeight, maxGrossWeight, originDepartmentId, destinationDepartmentId, sealNumber,
                created, modified, attributes, carrier);
    }

    public Optional<PalletCarrier> assignedCarrier() {
        return Optional.ofNullable(carrier);
    }

    public Optional<PalletLoadBlockReason> blockReason() {
        return Optional.ofNullable(loadBlockReason);
    }

    public boolean transportLocked() {
        return palletStatus == PalletStatus.IN_TRANSIT || palletStatus == PalletStatus.DELIVERED
                || palletStatus == PalletStatus.CANCELLED;
    }
}
