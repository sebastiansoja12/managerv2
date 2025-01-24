package com.warehouse.pallet.domain.port.primary;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.model.AssignDriverRequest;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.ShipmentAttachRequest;
import com.warehouse.pallet.domain.vo.SealNumber;

public interface PalletPort {
    void createPallet(final Pallet pallet);
    PalletId createEmptyPallet();
    void updatePallet(final Pallet pallet);
    void deletePallet(final PalletId palletId);
    Pallet getPallet(final PalletId palletId);
    void determinePalletHandlingPriority(final PalletId palletId);
    void addSealNumber(final PalletId palletId, final SealNumber sealNumber);
    void assignDriver(final AssignDriverRequest assignDriverRequest);
    void attachShipments(final ShipmentAttachRequest shipmentAttachRequest);
}
