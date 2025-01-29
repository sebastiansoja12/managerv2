package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.vo.SealNumber;

public class SealNumberRequest {
    private PalletId palletId;
    private SealNumber sealNumber;

    public SealNumberRequest(final PalletId palletId, final SealNumber sealNumber) {
        this.palletId = palletId;
        this.sealNumber = sealNumber;
    }

    public PalletId getPalletId() {
        return palletId;
    }

    public void setPalletId(final PalletId palletId) {
        this.palletId = palletId;
    }

    public SealNumber getSealNumber() {
        return sealNumber;
    }

    public void setSealNumber(final SealNumber sealNumber) {
        this.sealNumber = sealNumber;
    }
}
