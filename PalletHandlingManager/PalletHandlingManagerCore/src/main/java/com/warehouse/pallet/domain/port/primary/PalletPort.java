package com.warehouse.pallet.domain.port.primary;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.vo.DriverId;
import com.warehouse.pallet.domain.vo.SealNumber;

import java.util.Set;

public interface PalletPort {
    void createPallet(final Pallet pallet);
    void updatePallet(final Pallet pallet);
    void deletePallet(final Pallet pallet);
    Pallet getPallet(final PalletId palletId);
    void determinePalletHandlingPriority(final PalletId palletId);
    void addSealNumber(final PalletId palletId, final SealNumber sealNumber);
    void assignDriver(final PalletId palletId, final DriverId driverId);
    void attachShipments(final PalletId palletId, final Set<ShipmentId> shipmentIds);
}
