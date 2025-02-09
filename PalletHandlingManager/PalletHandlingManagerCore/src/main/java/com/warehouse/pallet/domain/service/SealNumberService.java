package com.warehouse.pallet.domain.service;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.vo.SealNumber;

public interface SealNumberService {
    void validateSealNumber(final PalletId palletId, final SealNumber sealNumber);
}
