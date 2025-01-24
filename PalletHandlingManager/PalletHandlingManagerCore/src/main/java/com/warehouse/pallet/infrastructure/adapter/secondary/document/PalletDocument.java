package com.warehouse.pallet.infrastructure.adapter.secondary.document;

import com.warehouse.pallet.configuration.identificator.DepartmentCode;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.enumeration.StorageStatus;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.Weight;
import com.warehouse.pallet.domain.vo.Dimension;
import com.warehouse.pallet.domain.vo.MaxPalletWeight;
import com.warehouse.pallet.domain.vo.SealNumber;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

@Document(collection = "PalletDocument")
public class PalletDocument {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "pallet_id"))
    private PalletId palletId;
    private Set<ShipmentId> shipmentIds;
    private DepartmentCode originDepartment;
    private DepartmentCode destinationDepartment;
    private Instant created;
    private Instant modified;
    private PalletStatus palletStatus;
    private StorageStatus storageStatus;
    private DriverDocument driver;
    private Weight palletWeight;
    private Dimension dimension;
    private PalletHandlingPriority palletHandlingPriority;
    private SealNumber sealNumber;
    private Boolean refrigerated;
    private MaxPalletWeight maxPalletWeight;

    public static PalletDocument from(final Pallet pallet) {
        return null;
    }

    public PalletDocument(final PalletId palletId,
                          final Set<ShipmentId> shipmentIds,
                          final DepartmentCode originDepartment,
                          final DepartmentCode destinationDepartment,
                          final Instant created,
                          final Instant modified,
                          final PalletStatus palletStatus,
                          final StorageStatus storageStatus,
                          final DriverDocument driver,
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
        this.created = created;
        this.modified = modified;
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

    public PalletId getPalletId() {
        return palletId;
    }

    public void setPalletId(final PalletId palletId) {
        this.palletId = palletId;
    }

    public Set<ShipmentId> getShipmentIds() {
        return shipmentIds;
    }

    public void setShipmentIds(final Set<ShipmentId> shipmentIds) {
        this.shipmentIds = shipmentIds;
    }

    public DepartmentCode getOriginDepartment() {
        return originDepartment;
    }

    public void setOriginDepartment(final DepartmentCode originDepartment) {
        this.originDepartment = originDepartment;
    }

    public DepartmentCode getDestinationDepartment() {
        return destinationDepartment;
    }

    public void setDestinationDepartment(final DepartmentCode destinationDepartment) {
        this.destinationDepartment = destinationDepartment;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(final Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(final Instant modified) {
        this.modified = modified;
    }

    public PalletStatus getPalletStatus() {
        return palletStatus;
    }

    public void setPalletStatus(final PalletStatus palletStatus) {
        this.palletStatus = palletStatus;
    }

    public StorageStatus getStorageStatus() {
        return storageStatus;
    }

    public void setStorageStatus(final StorageStatus storageStatus) {
        this.storageStatus = storageStatus;
    }

    public DriverDocument getDriver() {
        return driver;
    }

    public void setDriver(final DriverDocument driver) {
        this.driver = driver;
    }

    public Weight getPalletWeight() {
        return palletWeight;
    }

    public void setPalletWeight(final Weight palletWeight) {
        this.palletWeight = palletWeight;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(final Dimension dimension) {
        this.dimension = dimension;
    }

    public PalletHandlingPriority getPalletHandlingPriority() {
        return palletHandlingPriority;
    }

    public void setPalletHandlingPriority(final PalletHandlingPriority palletHandlingPriority) {
        this.palletHandlingPriority = palletHandlingPriority;
    }

    public SealNumber getSealNumber() {
        return sealNumber;
    }

    public void setSealNumber(final SealNumber sealNumber) {
        this.sealNumber = sealNumber;
    }

    public Boolean getRefrigerated() {
        return refrigerated;
    }

    public void setRefrigerated(final Boolean refrigerated) {
        this.refrigerated = refrigerated;
    }

    public MaxPalletWeight getMaxPalletWeight() {
        return maxPalletWeight;
    }

    public void setMaxPalletWeight(final MaxPalletWeight maxPalletWeight) {
        this.maxPalletWeight = maxPalletWeight;
    }
}
