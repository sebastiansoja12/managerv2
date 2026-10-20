package com.warehouse.logistics.application.port.primary;

import com.warehouse.logistics.application.port.primary.command.PalletScanCommand;
import com.warehouse.logistics.application.port.primary.result.PalletScanOutcome;


public interface PalletScanPort {
    PalletScanOutcome scan(final PalletScanCommand command);
}
