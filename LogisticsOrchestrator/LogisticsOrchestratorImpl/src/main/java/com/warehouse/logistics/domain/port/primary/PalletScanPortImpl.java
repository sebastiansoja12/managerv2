package com.warehouse.logistics.domain.port.primary;

import com.warehouse.logistics.domain.port.secondary.PalletScanServicePort;
import com.warehouse.logistics.domain.vo.PalletScanCommand;
import com.warehouse.logistics.domain.vo.PalletScanOutcome;

public class PalletScanPortImpl implements PalletScanPort {

    private final PalletScanServicePort palletScanServicePort;

    public PalletScanPortImpl(final PalletScanServicePort palletScanServicePort) {
        this.palletScanServicePort = palletScanServicePort;
    }

    @Override
    public PalletScanOutcome scan(final PalletScanCommand command) {
        return this.palletScanServicePort.scan(command);
    }
}
