package com.warehouse.pallet.domain.port.primary;

import com.warehouse.pallet.domain.model.PalletUpdate;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.model.AssignDriverRequest;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.PalletLoadedRequest;
import com.warehouse.pallet.domain.model.PalletPage;
import com.warehouse.pallet.domain.model.PalletSearchCriteria;
import com.warehouse.pallet.domain.model.SealNumberRequest;
import com.warehouse.pallet.domain.model.ShipmentAttachRequest;
import com.warehouse.pallet.configuration.identificator.OperatorId;
import com.warehouse.pallet.domain.vo.TrackingNumber;

import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.domain.model.Weight;

import java.math.BigDecimal;
import com.warehouse.pallet.domain.model.PalletHistoryEntry;

import java.util.List;
import java.util.Set;

public interface PalletPort {
    void createPallet(final Pallet pallet);
    Pallet createEmptyPallet(final OperatorId operatorId, final DepartmentId originDepartmentId);
    Pallet createPalletWithShipments(final OperatorId operatorId, final DepartmentId originDepartmentId,
                                     final Set<ShipmentId> shipmentIds);
    void updatePallet(final Pallet pallet);
    void deletePallet(final PalletId palletId);
    Pallet getPallet(final PalletId palletId, final OperatorId operatorId);
    Pallet getPalletByTrackingNumber(final TrackingNumber trackingNumber, final OperatorId operatorId);
    PalletPage findPallets(final PalletSearchCriteria criteria);
    Pallet cancelPallet(final PalletId palletId, final OperatorId operatorId);
    List<PalletHistoryEntry> getHistory(final PalletId palletId, final OperatorId operatorId);
    void handlePalletLoaded(final PalletLoadedRequest palletLoadedRequest);
    void determinePalletHandlingPriority(final PalletId palletId);
    void addSealNumber(final SealNumberRequest sealNumberRequest);
    void assignDriver(final AssignDriverRequest assignDriverRequest);
    void attachShipments(final ShipmentAttachRequest shipmentAttachRequest);
    Pallet removeShipment(final PalletId palletId, final ShipmentId shipmentId, final OperatorId operatorId);
    Pallet changeGrossWeight(final PalletId palletId, final Weight grossWeight, final OperatorId operatorId);
    Pallet defineMaxGrossWeight(final PalletId palletId, final BigDecimal maxGrossWeight, final OperatorId operatorId);
    Pallet updatePallet(final PalletId palletId, final PalletUpdate update, final OperatorId operatorId);
    Pallet changeStatus(final PalletId palletId, final PalletStatus targetStatus, final OperatorId operatorId);
    Pallet changeDepartments(final PalletId palletId, final DepartmentId originDepartmentId,
                             final DepartmentId destinationDepartmentId, final OperatorId operatorId);
}
