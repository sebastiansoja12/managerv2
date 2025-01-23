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
}
