package com.warehouse.palletmanagement.domain.model;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.domain.exception.PalletRequestRejectedException;
import com.warehouse.palletmanagement.domain.vo.PalletShipment;

import java.util.Optional;
import java.util.Set;

public final class PalletizableShipmentPolicy {

    private static final String SHIPMENT_NOT_PALLETIZABLE = "SHIPMENT_NOT_PALLETIZABLE";

    private static final Set<ShipmentStatus> PALLETIZABLE_STATUSES = Set.of(
            ShipmentStatus.CREATED, ShipmentStatus.PREPARED, ShipmentStatus.ACCEPTED);

    private PalletizableShipmentPolicy() {
    }

    public static boolean canBePutOnPallet(final ShipmentStatus shipmentStatus) {
        return PALLETIZABLE_STATUSES.contains(shipmentStatus);
    }

    public static void ensurePalletizable(final ShipmentId shipmentId, final Optional<PalletShipment> shipment) {
        final PalletShipment found = shipment.orElseThrow(() -> new PalletRequestRejectedException(404,
                SHIPMENT_NOT_PALLETIZABLE, "Shipment " + shipmentId.getValue() + " not found"));
        if (!canBePutOnPallet(found.status())) {
            throw new PalletRequestRejectedException(409, SHIPMENT_NOT_PALLETIZABLE,
                    "Shipment " + shipmentId.getValue() + " cannot be put on pallet in status " + found.status());
        }
    }
}
