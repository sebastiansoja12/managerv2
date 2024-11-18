package com.warehouse.pallet.domain.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.enumeration.StorageStatus;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.Weight;
import com.warehouse.pallet.domain.port.secondary.PalletRepository;
import com.warehouse.pallet.domain.vo.*;

import java.util.UUID;

public class PalletStorageServiceImpl implements PalletStorageService {

    private final PalletRepository palletRepository;

    public PalletStorageServiceImpl(final PalletRepository palletRepository) {
        this.palletRepository = palletRepository;
    }

    @Override
    public PalletId nextPalletId() {
        return new PalletId(UUID.randomUUID().toString());
    }

    @Override
    public void createPallet(final Pallet pallet) {
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeDestinationDepartment(final PalletId palletId, final DepartmentCode destinationDepartment) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.changeDestinationDepartment(destinationDepartment);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeOriginDepartment(final PalletId palletId, final DepartmentCode originDepartment) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.changeOriginDepartment(originDepartment);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void addShipment(final PalletId palletId, final ShipmentId shipmentId) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.addShipment(shipmentId);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void removeShipment(final PalletId palletId, final ShipmentId shipmentId) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.removeShipment(shipmentId);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changePalletStatus(final PalletId palletId, final PalletStatus palletStatus) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.changePalletStatus(palletStatus);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeStorageStatus(final PalletId palletId, final StorageStatus storageStatus) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.changeStorageStatus(storageStatus);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeDriver(final PalletId palletId, final DriverId driverId) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.changeDriver(null);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeDimension(final PalletId palletId, final Dimension dimension) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.changeDimension(dimension);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changePalletHandlingPriority(final PalletId palletId, final PalletHandlingPriority palletHandlingPriority) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.changePalletHandlingPriority(palletHandlingPriority);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeSealNumber(final PalletId palletId, final SealNumber sealNumber) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.changeSealNumber(sealNumber);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeRefrigerated(final PalletId palletId, final Boolean refrigerated) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.changeRefrigerated(refrigerated);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeMaxPalletWeight(final PalletId palletId, final Double maxPalletWeight) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.changeMaxPalletWeight(maxPalletWeight);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeWeight(final PalletId palletId, final Weight weight) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        pallet.changeWeight(weight);
        this.palletRepository.createOrUpdate(pallet);
    }
}
