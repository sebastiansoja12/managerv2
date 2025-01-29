package com.warehouse.pallet.domain.port.primary;

import java.util.Set;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.model.AssignDriverRequest;
import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.SealNumberRequest;
import com.warehouse.pallet.domain.model.ShipmentAttachRequest;
import com.warehouse.pallet.domain.service.DriverStorageService;
import com.warehouse.pallet.domain.service.PalletStorageService;
import com.warehouse.pallet.domain.service.SealNumberService;
import com.warehouse.pallet.domain.vo.DriverId;
import com.warehouse.pallet.domain.vo.SealNumber;

public class PalletPortImpl implements PalletPort {

    private final PalletStorageService palletStorageService;

    private final DriverStorageService driverStorageService;

    private final SealNumberService sealNumberService;

    public PalletPortImpl(final PalletStorageService palletStorageService,
                          final DriverStorageService driverStorageService,
                          final SealNumberService sealNumberService) {
        this.palletStorageService = palletStorageService;
        this.driverStorageService = driverStorageService;
        this.sealNumberService = sealNumberService;
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
        return this.palletStorageService.find(palletId);
    }

    @Override
    public void determinePalletHandlingPriority(final PalletId palletId) {

    }

    @Override
    public void addSealNumber(final SealNumberRequest sealNumberRequest) {
        final PalletId palletId = sealNumberRequest.getPalletId();
        final SealNumber sealNumber = sealNumberRequest.getSealNumber();
        this.sealNumberService.validateSealNumber(palletId, sealNumber);
        this.palletStorageService.changeSealNumber(palletId, sealNumber);
    }

    @Override
    public void assignDriver(final AssignDriverRequest assignDriverRequest) {
        final PalletId palletId = assignDriverRequest.getPalletId();
        final DriverId driverId = assignDriverRequest.getDriverId();
        final Driver driver = this.driverStorageService.find(driverId);
        this.palletStorageService.changeDriver(palletId, driver);
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
