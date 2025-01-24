package com.warehouse.pallet.domain.port.primary;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.model.AssignDriverRequest;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.ShipmentAttachRequest;
import com.warehouse.pallet.domain.service.PalletStorageService;
import com.warehouse.pallet.domain.vo.DriverId;
import com.warehouse.pallet.domain.vo.SealNumber;

import java.util.Set;

public class PalletPortImpl implements PalletPort {

    private final PalletStorageService palletStorageService;

    private final DriverStorageService driverStorageService;

    public PalletPortImpl(final PalletStorageService palletStorageService) {
        this.palletStorageService = palletStorageService;
    }

    @Override
    public PalletId createEmptyPallet() {
        final PalletId palletId = this.palletStorageService.nextPalletId();
        this.palletStorageService.createEmptyPallet(palletId);
        return palletId;
    }

    @Override
    public void createPallet(final Pallet pallet) {

    }

    @Override
    public void updatePallet(final Pallet pallet) {

    }

    @Override
    public void deletePallet(final PalletId palletId) {

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
    public void assignDriver(final AssignDriverRequest assignDriverRequest) {
        final PalletId palletId = assignDriverRequest.getPalletId();
        final DriverId driverId = assignDriverRequest.getDriverId();
        this.palletStorageService.changeDriver(palletId, driverId);
    }

    @Override
    public void attachShipments(final ShipmentAttachRequest shipmentAttachRequest) {
        final Set<ShipmentId> shipmentIds = shipmentAttachRequest.getShipmentIds();
        final PalletId palletId = shipmentAttachRequest.getPalletId();
        for (final ShipmentId shipmentId : shipmentIds) {
            this.palletStorageService.addShipment(palletId, shipmentId);
        }
    }
}
