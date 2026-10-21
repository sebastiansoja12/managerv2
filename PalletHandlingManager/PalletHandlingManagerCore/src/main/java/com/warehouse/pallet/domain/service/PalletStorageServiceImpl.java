package com.warehouse.pallet.domain.service;


import com.warehouse.pallet.domain.enumeration.PalletType;
import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.enumeration.StorageStatus;
import com.warehouse.pallet.domain.exception.PalletNotFoundException;
import com.warehouse.pallet.domain.exception.ShipmentAlreadyOnPalletException;
import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.PalletPage;
import com.warehouse.pallet.domain.model.PalletSearchCriteria;
import com.warehouse.pallet.domain.model.Weight;
import com.warehouse.pallet.domain.port.secondary.PalletRepository;
import com.warehouse.pallet.domain.vo.Dimension;
import com.warehouse.pallet.configuration.identificator.OperatorId;
import com.warehouse.pallet.domain.vo.SealNumber;
import com.warehouse.pallet.domain.vo.TrackingNumber;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class PalletStorageServiceImpl implements PalletStorageService {

    private static final int TRACKING_NUMBER_ATTEMPTS = 10;

    private final PalletRepository palletRepository;
    private final PalletTrackingNumberGenerator trackingNumberGenerator;

    public PalletStorageServiceImpl(final PalletRepository palletRepository,
                                    final PalletTrackingNumberGenerator trackingNumberGenerator) {
        this.palletRepository = palletRepository;
        this.trackingNumberGenerator = trackingNumberGenerator;
    }

    @Override
    public PalletId nextPalletId() {
        return new PalletId(UUID.randomUUID().toString());
    }

    @Override
    public TrackingNumber nextTrackingNumber() {
        for (int attempt = 0; attempt < TRACKING_NUMBER_ATTEMPTS; attempt++) {
            final TrackingNumber trackingNumber = this.trackingNumberGenerator.generate();
            if (!this.palletRepository.existsByTrackingNumber(trackingNumber)) {
                return trackingNumber;
            }
        }
        throw new IllegalStateException("Cannot generate unique pallet tracking number");
    }

    @Override
    public Pallet createEmptyPallet(final PalletId palletId, final TrackingNumber trackingNumber,
                                    final OperatorId operatorId, final DepartmentId originDepartmentId) {
        final Pallet pallet = Pallet.empty(palletId, trackingNumber, operatorId);
        pallet.changeOriginDepartmentId(originDepartmentId);
        this.palletRepository.createOrUpdate(pallet);
        return pallet;
    }

    @Override
    public Pallet createPalletWithShipments(final PalletId palletId, final TrackingNumber trackingNumber,
                                            final OperatorId operatorId, final DepartmentId originDepartmentId,
                                            final Set<ShipmentId> shipmentIds) {
        shipmentIds.forEach(shipmentId -> ensureShipmentIsNotOnActivePallet(shipmentId, palletId));
        final Pallet pallet = Pallet.withShipments(palletId, trackingNumber, operatorId, shipmentIds);
        pallet.changeOriginDepartmentId(originDepartmentId);
        this.palletRepository.createOrUpdate(pallet);
        return pallet;
    }

    @Override
    public void createPallet(final Pallet pallet) {
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeDestinationDepartmentId(final PalletId palletId, final DepartmentId destinationDepartmentId) {
        final Pallet pallet = find(palletId);
        pallet.changeDestinationDepartmentId(destinationDepartmentId);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeOriginDepartmentId(final PalletId palletId, final DepartmentId originDepartmentId) {
        final Pallet pallet = find(palletId);
        pallet.changeOriginDepartmentId(originDepartmentId);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void addShipment(final PalletId palletId, final ShipmentId shipmentId) {
        final Pallet pallet = find(palletId);
        ensureShipmentIsNotOnActivePallet(shipmentId, palletId);
        pallet.addShipment(shipmentId);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void removeShipment(final PalletId palletId, final ShipmentId shipmentId) {
        final Pallet pallet = find(palletId);
        pallet.removeShipment(shipmentId);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changePalletStatus(final PalletId palletId, final PalletStatus palletStatus) {
        final Pallet pallet = find(palletId);
        pallet.changePalletStatus(palletStatus);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeStorageStatus(final PalletId palletId, final StorageStatus storageStatus) {
        final Pallet pallet = find(palletId);
        pallet.changeStorageStatus(storageStatus);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeDriver(final PalletId palletId, final Driver driver) {
        final Pallet pallet = find(palletId);
        pallet.changeDriver(driver);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeDimension(final PalletId palletId, final Dimension dimension) {
        final Pallet pallet = find(palletId);
        pallet.changeDimension(dimension);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changePalletHandlingPriority(final PalletId palletId, final PalletHandlingPriority palletHandlingPriority) {
        final Pallet pallet = find(palletId);
        pallet.changePalletHandlingPriority(palletHandlingPriority);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeSealNumber(final PalletId palletId, final SealNumber sealNumber) {
        final Pallet pallet = find(palletId);
        pallet.changeSealNumber(sealNumber);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeRefrigerated(final PalletId palletId, final Boolean refrigerated) {
        final Pallet pallet = find(palletId);
        pallet.changeRefrigerated(refrigerated);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeMaxPalletWeight(final PalletId palletId, final BigDecimal maxPalletWeight) {
        final Pallet pallet = find(palletId);
        pallet.changeMaxPalletWeight(maxPalletWeight);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public void changeWeight(final PalletId palletId, final Weight weight) {
        final Pallet pallet = find(palletId);
        pallet.changeWeight(weight);
        this.palletRepository.createOrUpdate(pallet);
    }

    @Override
    public Pallet find(final PalletId palletId) {
        final Pallet pallet = this.palletRepository.findById(palletId);
        if (pallet == null) {
            throw new PalletNotFoundException("Pallet " + palletId.value() + " not found");
        }
        return pallet;
    }

    @Override
    public Pallet findByTrackingNumber(final TrackingNumber trackingNumber) {
        return this.palletRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new PalletNotFoundException(
                        "Pallet with tracking number " + trackingNumber.value() + " not found"));
    }

    @Override
    public PalletPage search(final PalletSearchCriteria criteria) {
        return this.palletRepository.search(criteria);
    }

    @Override
    public Pallet cancel(final PalletId palletId) {
        final Pallet pallet = find(palletId);
        if (pallet.cancel()) {
            this.palletRepository.createOrUpdate(pallet);
        }
        return pallet;
    }

    @Override
    public boolean markLoaded(final PalletId palletId) {
        final Pallet pallet = find(palletId);
        final boolean changed = pallet.markLoaded();
        if (changed) {
            this.palletRepository.createOrUpdate(pallet);
        }
        return changed;
    }

    @Override
    public Pallet changeGrossWeight(final PalletId palletId, final Weight grossWeight) {
        final Pallet pallet = find(palletId);
        pallet.changeWeight(grossWeight);
        this.palletRepository.createOrUpdate(pallet);
        return pallet;
    }

    @Override
    public Pallet defineMaxGrossWeight(final PalletId palletId, final BigDecimal maxGrossWeight) {
        final Pallet pallet = find(palletId);
        pallet.changeMaxPalletWeight(maxGrossWeight);
        this.palletRepository.createOrUpdate(pallet);
        return pallet;
    }

    @Override
    public Pallet update(final PalletId palletId, final PalletType palletType,
                         final PalletHandlingPriority palletHandlingPriority, final Boolean refrigerated,
                         final Dimension dimension, final SealNumber sealNumber) {
        final Pallet pallet = find(palletId);
        pallet.update(palletType, palletHandlingPriority, refrigerated, dimension, sealNumber);
        this.palletRepository.createOrUpdate(pallet);
        return pallet;
    }

    @Override
    public PalletStatus changeTransportStatus(final PalletId palletId, final PalletStatus targetStatus) {
        final Pallet pallet = find(palletId);
        final PalletStatus previousStatus = pallet.changeTransportStatus(targetStatus);
        this.palletRepository.createOrUpdate(pallet);
        return previousStatus;
    }

    @Override
    public Pallet changeDepartments(final PalletId palletId, final DepartmentId originDepartmentId,
                                    final DepartmentId destinationDepartmentId) {
        final Pallet pallet = find(palletId);
        pallet.changeOriginDepartmentId(originDepartmentId);
        pallet.changeDestinationDepartmentId(destinationDepartmentId);
        this.palletRepository.createOrUpdate(pallet);
        return pallet;
    }

    private void ensureShipmentIsNotOnActivePallet(final ShipmentId shipmentId, final PalletId palletId) {
        final Optional<PalletId> activePalletId = this.palletRepository.findActivePalletIdContaining(shipmentId);
        if (activePalletId.isPresent() && !activePalletId.get().equals(palletId)) {
            throw new ShipmentAlreadyOnPalletException(
                    "Shipment " + shipmentId.value() + " is already on pallet " + activePalletId.get().value());
        }
    }
}
