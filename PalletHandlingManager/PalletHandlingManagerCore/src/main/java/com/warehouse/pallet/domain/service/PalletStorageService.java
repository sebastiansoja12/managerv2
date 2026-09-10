package com.warehouse.pallet.domain.service;


import com.warehouse.pallet.domain.enumeration.PalletType;
import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.enumeration.StorageStatus;
import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.PalletPage;
import com.warehouse.pallet.domain.model.PalletSearchCriteria;
import com.warehouse.pallet.domain.model.Weight;
import com.warehouse.pallet.domain.vo.Dimension;
import com.warehouse.pallet.configuration.identificator.OperatorId;
import com.warehouse.pallet.domain.vo.SealNumber;
import com.warehouse.pallet.domain.vo.TrackingNumber;

import java.math.BigDecimal;
import java.util.Set;

public interface PalletStorageService {
    PalletId nextPalletId();
    TrackingNumber nextTrackingNumber();
    Pallet createEmptyPallet(final PalletId palletId, final TrackingNumber trackingNumber, final OperatorId operatorId,
                             final DepartmentId originDepartmentId);
    Pallet createPalletWithShipments(final PalletId palletId, final TrackingNumber trackingNumber,
                                     final OperatorId operatorId, final DepartmentId originDepartmentId,
                                     final Set<ShipmentId> shipmentIds);
    void createPallet(final Pallet pallet);
    void changeDestinationDepartmentId(final PalletId palletId, final DepartmentId destinationDepartmentId);
    void changeOriginDepartmentId(final PalletId palletId, final DepartmentId originDepartmentId);
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
    Pallet findByTrackingNumber(final TrackingNumber trackingNumber);
    PalletPage search(final PalletSearchCriteria criteria);
    Pallet cancel(final PalletId palletId);
    boolean markLoaded(final PalletId palletId);
    Pallet changeGrossWeight(final PalletId palletId, final Weight grossWeight);
    Pallet defineMaxGrossWeight(final PalletId palletId, final BigDecimal maxGrossWeight);
    Pallet update(final PalletId palletId, final PalletType palletType,
                  final PalletHandlingPriority palletHandlingPriority, final Boolean refrigerated,
                  final Dimension dimension, final SealNumber sealNumber);
    PalletStatus changeTransportStatus(final PalletId palletId, final PalletStatus targetStatus);
    Pallet changeDepartments(final PalletId palletId, final DepartmentId originDepartmentId,
                             final DepartmentId destinationDepartmentId);
}
