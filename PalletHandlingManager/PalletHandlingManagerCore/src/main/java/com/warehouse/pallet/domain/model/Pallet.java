package com.warehouse.pallet.domain.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.enumeration.StorageStatus;
import com.warehouse.pallet.domain.vo.Dimension;
import com.warehouse.pallet.domain.vo.MaxPalletWeight;
import com.warehouse.pallet.domain.vo.PalletId;
import com.warehouse.pallet.domain.vo.SealNumber;

public class Pallet {
    private PalletId palletId;
    private Set<ShipmentId> shipmentIds;
    private DepartmentCode originDepartment;
    private DepartmentCode destinationDepartment;
    private Instant created;
    private Instant modified;
    private PalletStatus palletStatus;
    private StorageStatus storageStatus;
    private Driver driver;
    private Weight palletWeight;
    private Dimension dimension;
    private PalletHandlingPriority palletHandlingPriority;
    private SealNumber sealNumber;
    private Boolean refrigerated;
    private MaxPalletWeight maxPalletWeight;

	public Pallet(final PalletId palletId,
                  final Set<ShipmentId> shipmentIds,
                  final DepartmentCode originDepartment,
                  final DepartmentCode destinationDepartment,
                  final PalletStatus palletStatus,
                  final StorageStatus storageStatus,
                  final Driver driver,
                  final Weight palletWeight,
                  final Dimension dimension,
                  final PalletHandlingPriority palletHandlingPriority,
                  final SealNumber sealNumber,
                  final Boolean refrigerated,
                  final MaxPalletWeight maxPalletWeight) {
        this.palletId = palletId;
        this.shipmentIds = shipmentIds;
        this.originDepartment = originDepartment;
        this.destinationDepartment = destinationDepartment;
        this.created = Instant.now();
        this.modified = Instant.now();
        this.palletStatus = palletStatus;
        this.storageStatus = storageStatus;
        this.driver = driver;
        this.palletWeight = palletWeight;
        this.dimension = dimension;
        this.palletHandlingPriority = palletHandlingPriority;
        this.sealNumber = sealNumber;
        this.refrigerated = refrigerated;
        this.maxPalletWeight = maxPalletWeight;
    }

    public DepartmentCode getDestinationDepartment() {
        return destinationDepartment;
    }

    public PalletId getPalletId() {
        return palletId;
    }

    public Set<ShipmentId> getShipmentIds() {
        if (this.shipmentIds == null) {
            this.shipmentIds = new HashSet<>();
        }
        return this.shipmentIds;
    }

    public DepartmentCode getOriginDepartment() {
        return originDepartment;
    }

    public Instant getCreated() {
        return created;
    }

    public Instant getModified() {
        return modified;
    }

    public PalletStatus getPalletStatus() {
        return palletStatus;
    }

    public StorageStatus getStorageStatus() {
        return storageStatus;
    }

    public Driver getDriver() {
        return driver;
    }

    public Weight getPalletWeight() {
        return palletWeight;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public PalletHandlingPriority getPalletHandlingPriority() {
        return palletHandlingPriority;
    }

    public SealNumber getSealNumber() {
        return sealNumber;
    }

    public Boolean getRefrigerated() {
        return refrigerated;
    }

    public MaxPalletWeight getMaxPalletWeight() {
        return maxPalletWeight;
    }

    public void changeOriginDepartment(final DepartmentCode originDepartment) {
        this.originDepartment = originDepartment;
        modified();
    }

    public void changeDestinationDepartment(final DepartmentCode destinationDepartment) {
        this.destinationDepartment = destinationDepartment;
        modified();
    }

    public void addShipment(final ShipmentId shipmentId) {
        this.getShipmentIds().add(shipmentId);
        modified();
    }

    public void removeShipment(final ShipmentId shipmentId) {
        this.getShipmentIds().remove(shipmentId);
        modified();
    }

    public void changePalletStatus(final PalletStatus palletStatus) {
        this.palletStatus = palletStatus;
        modified();
    }

    public void changeStorageStatus(final StorageStatus storageStatus) {
        this.storageStatus = storageStatus;
        modified();
    }

    public void changeDriver(final Driver driver) {
        this.driver = driver;
        modified();
    }

    public void changeDimension(final Dimension dimension) {
        this.dimension = dimension;
        modified();
    }

    public void changePalletHandlingPriority(final PalletHandlingPriority palletHandlingPriority) {
        this.palletHandlingPriority = palletHandlingPriority;
        modified();
    }

    public void changeSealNumber(final SealNumber sealNumber) {
        this.sealNumber = sealNumber;
        modified();
    }

    public void changeRefrigerated(final Boolean refrigerated) {
        this.refrigerated = refrigerated;
        modified();
    }

    public void changeMaxPalletWeight(final Double maxPalletWeight) {
        this.maxPalletWeight = new MaxPalletWeight(maxPalletWeight);
        modified();
    }

    public void changeWeight(final Weight palletWeight) {
        this.palletWeight = palletWeight;
        modified();
    }

    private void modified() {
        this.modified = Instant.now();
    }


}
