package com.warehouse.pallet.domain.service;


import com.warehouse.pallet.configuration.identificator.DepartmentCode;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.enumeration.StorageStatus;
import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.Weight;
import com.warehouse.pallet.domain.vo.Dimension;
import com.warehouse.pallet.domain.vo.SealNumber;

import java.math.BigDecimal;

public interface PalletStorageService {
    PalletId nextPalletId();
    void createEmptyPallet(final PalletId palletId);
    void createPallet(final Pallet pallet);
    void changeDestinationDepartment(final PalletId palletId, final DepartmentCode destinationDepartment);
    void changeOriginDepartment(final PalletId palletId, final DepartmentCode originDepartment);
    void addShipment(final PalletId palletId, final ShipmentId shipmentId);
    void removeShipment(final PalletId palletId, final ShipmentId shipmentId);
    void changePalletStatus(final PalletId palletId, final PalletStatus palletStatus);
    void changeStorageStatus(final PalletId palletId, final StorageStatus storageStatus);
    void changeDriver(final PalletId palletId, final Driver driver);
    void changeDimension(final PalletId palletId, final Dimension dimension);
    void changePalletHandlingPriority(final PalletId palletId, final PalletHandlingPriority palletHandlingPriority);
    void changeSealNumber(final PalletId palletId, final SealNumber sealNumber);
    void changeRefrigerated(final PalletId palletId, final Boolean refrigerated);
    void changeMaxPalletWeight(final PalletId palletId, final BigDecimal maxPalletWeight);
    void changeWeight(final PalletId palletId, final Weight weight);

    Pallet find(final PalletId palletId);
}
