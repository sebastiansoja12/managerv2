package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.vo.DriverId;

public class AssignDriverRequest {
    private PalletId palletId;
    private DriverId driverId;

    public AssignDriverRequest(final PalletId palletId, final DriverId driverId) {
        this.palletId = palletId;
        this.driverId = driverId;
    }

    public PalletId getPalletId() {
        return palletId;
    }

    public void setPalletId(final PalletId palletId) {
        this.palletId = palletId;
    }

    public DriverId getDriverId() {
        return driverId;
    }

    public void setDriverId(final DriverId driverId) {
        this.driverId = driverId;
    }
}
