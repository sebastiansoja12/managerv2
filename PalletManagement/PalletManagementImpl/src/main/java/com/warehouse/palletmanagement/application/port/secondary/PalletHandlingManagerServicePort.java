package com.warehouse.palletmanagement.application.port.secondary;

import com.warehouse.palletmanagement.domain.vo.PalletUpdate;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.domain.vo.PalletDepartmentsChange;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletHistoryEntry;
import com.warehouse.palletmanagement.domain.vo.PalletWeight;

import java.math.BigDecimal;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.palletmanagement.domain.vo.PalletPage;
import com.warehouse.palletmanagement.domain.vo.PalletSearchCriteria;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;

import java.util.List;

public interface PalletHandlingManagerServicePort {
    PalletDetails createEmptyPallet();
    PalletDetails createPallet(final List<ShipmentId> shipmentIds);
    PalletDetails getByTrackingNumber(final PalletTrackingNumber trackingNumber);
    PalletPage findPallets(final PalletSearchCriteria criteria);
    PalletDetails cancelPallet(final PalletId palletId);
    PalletDetails addShipments(final PalletId palletId, final List<ShipmentId> shipmentIds);
    PalletDetails removeShipment(final PalletId palletId, final ShipmentId shipmentId);
    PalletDetails changeGrossWeight(final PalletId palletId, final PalletWeight grossWeight);
    PalletDetails defineMaxGrossWeight(final PalletId palletId, final BigDecimal maxGrossWeight);
    List<PalletHistoryEntry> getHistory(final PalletId palletId);
    PalletDetails getPallet(final PalletId palletId);
    PalletDetails updatePallet(final PalletId palletId, final PalletUpdate update);
    PalletDetails changeStatus(final PalletId palletId, final PalletStatus targetStatus);
    PalletDetails changeDepartments(final PalletId palletId, final PalletDepartmentsChange departmentsChange);
}
