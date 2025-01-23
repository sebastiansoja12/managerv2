package com.warehouse.pallet.domain.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.enumeration.StorageStatus;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.Weight;
import com.warehouse.pallet.domain.vo.Dimension;
import com.warehouse.pallet.domain.vo.DriverId;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.vo.SealNumber;

public interface PalletStorageService {
    PalletId nextPalletId();
    void createPallet(final Pallet pallet);
    void changeDestinationDepartment(final PalletId palletId, final DepartmentCode destinationDepartment);
    void changeOriginDepartment(final PalletId palletId, final DepartmentCode originDepartment);
    void addShipment(final PalletId palletId, final ShipmentId shipmentId);
    void removeShipment(final PalletId palletId, final ShipmentId shipmentId);
    void changePalletStatus(final PalletId palletId, final PalletStatus palletStatus);
    void changeStorageStatus(final PalletId palletId, final StorageStatus storageStatus);
    void changeDriver(final PalletId palletId, final DriverId driverId);
    void changeDimension(final PalletId palletId, final Dimension dimension);
    void changePalletHandlingPriority(final PalletId palletId, final PalletHandlingPriority palletHandlingPriority);
    void changeSealNumber(final PalletId palletId, final SealNumber sealNumber);
    void changeRefrigerated(final PalletId palletId, final Boolean refrigerated);
    void changeMaxPalletWeight(final PalletId palletId, final Double maxPalletWeight);
    void changeWeight(final PalletId palletId, final Weight weight);
}
