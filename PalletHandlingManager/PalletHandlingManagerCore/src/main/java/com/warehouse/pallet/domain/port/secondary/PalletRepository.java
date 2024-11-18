package com.warehouse.pallet.domain.port.secondary;

import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.vo.PalletId;

public interface PalletRepository {
    void createOrUpdate(final Pallet pallet);
    Pallet findById(final PalletId palletId);
}
