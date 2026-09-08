package com.warehouse.logistics.application.port.secondary;

import com.warehouse.logistics.application.port.primary.command.PalletScanCommand;
import com.warehouse.logistics.application.port.primary.result.PalletScanOutcome;


public interface PalletScanServicePort {
    PalletScanOutcome scan(final PalletScanCommand command);
}
