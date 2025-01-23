package com.warehouse.pallet.domain.port.primary;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.vo.DriverId;
import com.warehouse.pallet.domain.vo.SealNumber;

import java.util.Set;

public class PalletPortImpl implements PalletPort {

    @Override
    public PalletId createEmptyPallet() {
        return null;
    }

    @Override
    public void createPallet(final Pallet pallet) {

    }

    @Override
    public void updatePallet(final Pallet pallet) {

    }

    @Override
    public void deletePallet(final Pallet pallet) {

    }

    @Override
    public Pallet getPallet(final PalletId palletId) {
        return null;
    }

    @Override
    public void determinePalletHandlingPriority(final PalletId palletId) {

    }

    @Override
    public void addSealNumber(final PalletId palletId, final SealNumber sealNumber) {

    }

    @Override
    public void assignDriver(final PalletId palletId, final DriverId driverId) {

    }

    @Override
    public void attachShipments(final PalletId palletId, final Set<ShipmentId> shipmentIds) {

    }
}
