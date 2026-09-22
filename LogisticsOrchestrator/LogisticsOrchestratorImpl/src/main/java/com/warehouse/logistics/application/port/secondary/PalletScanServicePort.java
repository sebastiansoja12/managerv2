package com.warehouse.logistics.application.port.secondary;

import com.warehouse.logistics.application.port.primary.command.PalletScanCommand;
import com.warehouse.logistics.application.port.primary.command.PalletUnloadCommand;
import com.warehouse.logistics.application.port.primary.result.PalletScanOutcome;
import com.warehouse.logistics.application.port.primary.result.PalletUnloadOutcome;


public interface PalletScanServicePort {
    PalletScanOutcome scan(final PalletScanCommand command);

    PalletUnloadOutcome unload(final PalletUnloadCommand command);
}
