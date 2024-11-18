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

}
