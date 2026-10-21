package com.warehouse.logistics.application.port.primary;

import com.warehouse.logistics.application.port.primary.command.PalletScanCommand;
import com.warehouse.logistics.application.port.primary.result.PalletScanOutcome;
import com.warehouse.logistics.application.port.secondary.PalletScanServicePort;


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
