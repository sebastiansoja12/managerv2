package com.warehouse.departmentmap.application.port.primary;

import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.departmentmap.application.port.primary.command.ReserveLocationCommand;
import com.warehouse.departmentmap.application.port.primary.result.PalletLocationResult;
import com.warehouse.departmentmap.domain.model.PalletMovement;
import com.warehouse.departmentmap.domain.model.StorageLocation;

import java.util.List;
import java.util.Optional;

public interface PalletLocationPort {
    PalletLocationResult placePallet(final MapElementId locationId, final PalletTrackingNumber palletTrackingNumber);
    PalletLocationResult movePallet(final PalletTrackingNumber palletTrackingNumber, final MapElementId targetLocationId);
    void removePallet(final PalletTrackingNumber palletTrackingNumber);
    StorageLocation reserveLocation(final MapElementId locationId, final ReserveLocationCommand command);
    StorageLocation cancelReservation(final MapElementId locationId);
    Optional<PalletLocationResult> findPalletLocation(final PalletTrackingNumber palletTrackingNumber);
    List<PalletLocationResult> searchPallets(final String query);
    List<PalletMovement> palletHistory(final PalletTrackingNumber palletTrackingNumber);
}
