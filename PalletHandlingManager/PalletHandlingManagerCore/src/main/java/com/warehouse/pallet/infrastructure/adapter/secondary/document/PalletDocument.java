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
import com.warehouse.pallet.infrastructure.adapter.secondary.enumeration.PalletType;
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
    private final PalletId palletId;
    private final Set<ShipmentId> shipmentIds;
    private final DepartmentCode originDepartment;
    private final DepartmentCode destinationDepartment;
    private final Instant created;
    private final Instant modified;
    private final PalletStatus palletStatus;
    private final StorageStatus storageStatus;
    private final DriverDocument driver;
    private final Weight palletWeight;
    private final Dimension dimension;
    private final PalletHandlingPriority palletHandlingPriority;
    private final PalletType palletType;
    private final SealNumber sealNumber;
    private final Boolean refrigerated;
    private final MaxPalletWeight maxPalletWeight;

	public static PalletDocument from(final Pallet pallet) {
		return new PalletDocument(pallet.getPalletId(), pallet.getShipmentIds(), pallet.getOriginDepartment(),
				pallet.getDestinationDepartment(), pallet.getCreated(), pallet.getModified(), pallet.getPalletStatus(),
				pallet.getStorageStatus(), DriverDocument.from(pallet.getDriver()), pallet.getPalletWeight(),
				pallet.getDimension(), pallet.getPalletHandlingPriority(),
				PalletType.valueOf(pallet.getPalletType().name()), pallet.getSealNumber(), pallet.isRefrigerated(),
				pallet.getMaxPalletWeight());
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

    public Instant getCreated() {
        return created;
    }

    public DepartmentCode getDestinationDepartment() {
        return destinationDepartment;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public DriverDocument getDriver() {
        return driver;
    }

    public MaxPalletWeight getMaxPalletWeight() {
        return maxPalletWeight;
    }

    public Instant getModified() {
        return modified;
    }

    public DepartmentCode getOriginDepartment() {
        return originDepartment;
    }

    public PalletHandlingPriority getPalletHandlingPriority() {
        return palletHandlingPriority;
    }

    public PalletId getPalletId() {
        return palletId;
    }

    public PalletStatus getPalletStatus() {
        return palletStatus;
    }

    public PalletType getPalletType() {
        return palletType;
    }

    public Weight getPalletWeight() {
        return palletWeight;
    }

    public Boolean isRefrigerated() {
        return refrigerated;
    }

    public SealNumber getSealNumber() {
        return sealNumber;
    }

    public Set<ShipmentId> getShipmentIds() {
        return shipmentIds;
    }

    public StorageStatus getStorageStatus() {
        return storageStatus;
    }
}
