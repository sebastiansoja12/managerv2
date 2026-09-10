package com.warehouse.palletmanagement.application.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.application.port.primary.command.AddPalletShipmentsCommand;
import com.warehouse.palletmanagement.application.port.primary.command.CreatePalletCommand;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.vo.PalletCarrierAssignment;
import com.warehouse.palletmanagement.domain.vo.PalletDepartmentsChange;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletDocumentFile;
import com.warehouse.palletmanagement.domain.vo.PalletHistoryEntry;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.palletmanagement.domain.vo.PalletPage;
import com.warehouse.palletmanagement.domain.vo.PalletSearchCriteria;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.palletmanagement.domain.vo.PalletUpdate;

import java.math.BigDecimal;
import java.util.List;

public interface PalletPort {
    PalletDetails createEmptyPallet();
    PalletDetails createPallet(final CreatePalletCommand command);
    PalletDetails getPallet(final PalletTrackingNumber trackingNumber);

    PalletDetails getPallet(final PalletId palletId);
    PalletPage findPallets(final PalletSearchCriteria criteria);
    PalletDetails cancelPallet(final PalletId palletId);
    PalletDetails addShipments(final AddPalletShipmentsCommand command);
    PalletDetails removeShipment(final PalletId palletId, final ShipmentId shipmentId);
    PalletDetails defineMaxGrossWeight(final PalletId palletId, final BigDecimal maxGrossWeight);
    PalletDetails updatePallet(final PalletId palletId, final PalletUpdate update);
    List<PalletHistoryEntry> getHistory(final PalletId palletId);
    PalletDetails assignCarrier(final PalletId palletId, final PalletCarrierAssignment assignment);
    PalletDetails changeStatus(final PalletId palletId, final PalletStatus targetStatus);
    PalletDocumentFile generateLabel(final PalletTrackingNumber trackingNumber);
    PalletDocumentFile generateTransportDocument(final PalletTrackingNumber trackingNumber);
    PalletDetails changeDepartments(final PalletId palletId, final PalletDepartmentsChange departmentsChange);
}
