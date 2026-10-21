package com.warehouse.palletmanagement.application.port.primary;

import com.warehouse.palletmanagement.application.port.primary.command.PalletScanCommand;
import com.warehouse.palletmanagement.application.port.primary.result.PalletScanResult;

public interface PalletScanPort {
    PalletScanResult scan(final PalletScanCommand command);
}
