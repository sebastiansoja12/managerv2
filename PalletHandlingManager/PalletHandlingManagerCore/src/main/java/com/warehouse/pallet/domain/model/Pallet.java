package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.configuration.identificator.DepartmentCode;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.*;
import com.warehouse.pallet.domain.vo.Dimension;
import com.warehouse.pallet.domain.vo.MaxPalletWeight;
import com.warehouse.pallet.domain.vo.SealNumber;
import com.warehouse.pallet.infrastructure.adapter.secondary.document.PalletDocument;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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
    private PalletType palletType;
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
                  final PalletType palletType,
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
        this.palletType = palletType;
        this.sealNumber = sealNumber;
        this.refrigerated = refrigerated;
        this.maxPalletWeight = maxPalletWeight;
    }

    public Pallet(final PalletId palletId,
                  final Set<ShipmentId> shipmentIds,
                  final DepartmentCode originDepartment,
                  final DepartmentCode destinationDepartment,
                  final Instant created,
                  final Instant modified,
                  final PalletStatus palletStatus,
                  final StorageStatus storageStatus,
                  final Driver driver,
                  final Weight palletWeight,
                  final Dimension dimension,
                  final PalletHandlingPriority palletHandlingPriority,
                  final PalletType palletType,
                  final SealNumber sealNumber,
                  final Boolean refrigerated,
                  final MaxPalletWeight maxPalletWeight) {
        this.palletId = palletId;
        this.shipmentIds = shipmentIds;
        this.originDepartment = originDepartment;
        this.destinationDepartment = destinationDepartment;
        this.created = created;
        this.modified = modified;
        this.palletStatus = palletStatus;
        this.storageStatus = storageStatus;
        this.driver = driver;
        this.palletWeight = palletWeight;
        this.dimension = dimension;
        this.palletHandlingPriority = palletHandlingPriority;
        this.palletType = palletType;
        this.sealNumber = sealNumber;
        this.refrigerated = refrigerated;
        this.maxPalletWeight = maxPalletWeight;
    }

    public Pallet(final PalletId palletId) {
        this.palletId = palletId;
        this.shipmentIds = new HashSet<>();
        this.originDepartment = new DepartmentCode(StringUtils.EMPTY);
        this.destinationDepartment = new DepartmentCode(StringUtils.EMPTY);
        this.created = Instant.now();
        this.modified = Instant.now();
        this.palletStatus = PalletStatus.EMPTY;
        this.storageStatus = StorageStatus.UNLOCKED;
        this.driver = Driver.empty();
        this.palletWeight = null;
        this.dimension = null;
        this.palletHandlingPriority = PalletHandlingPriority.LOW;
        this.sealNumber = null;
        this.refrigerated = false;
        this.maxPalletWeight = null;
        this.palletType = PalletType.PARENT;
    }

    public static Pallet from(final PalletDocument pallet) {
		return new Pallet(pallet.getPalletId(), pallet.getShipmentIds(), pallet.getOriginDepartment(),
				pallet.getDestinationDepartment(), pallet.getCreated(), pallet.getModified(), pallet.getPalletStatus(),
				pallet.getStorageStatus(), Driver.from(pallet.getDriver()), pallet.getPalletWeight(),
				pallet.getDimension(), pallet.getPalletHandlingPriority(),
				PalletType.valueOf(pallet.getPalletType().name()), pallet.getSealNumber(), pallet.isRefrigerated(),
				pallet.getMaxPalletWeight());
    }

    public static Pallet empty(final PalletId palletId) {
        return new Pallet(palletId);
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

    public Boolean isRefrigerated() {
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
        checkPalletLocked();

        this.getShipmentIds().add(shipmentId);
        modified();
    }

    public void removeShipment(final ShipmentId shipmentId) {
        checkPalletLocked();

        this.getShipmentIds().remove(shipmentId);
        modified();
    }

    private void checkPalletLocked() {
        if (this.storageStatus.equals(StorageStatus.LOCKED)) {
            throw new IllegalStateException("Pallet is locked");
        }
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

    public void changeMaxPalletWeight(final BigDecimal maxPalletWeight) {
        this.maxPalletWeight = new MaxPalletWeight(maxPalletWeight, Unit.KG);
        modified();
    }

    public void changeWeight(final Weight palletWeight) {
        this.palletWeight = palletWeight;
        modified();
    }

    public PalletType getPalletType() {
        return palletType;
    }

    private void modified() {
        this.modified = Instant.now();
    }
}
