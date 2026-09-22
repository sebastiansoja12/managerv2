package com.warehouse.logistics.application.port.primary;

import com.warehouse.logistics.application.port.primary.command.PalletScanCommand;
import com.warehouse.logistics.application.port.primary.command.PalletUnloadCommand;
import com.warehouse.logistics.application.port.primary.result.PalletScanOutcome;
import com.warehouse.logistics.application.port.primary.result.PalletUnloadOutcome;


public interface PalletScanPort {
    PalletScanOutcome scan(final PalletScanCommand command);

    PalletUnloadOutcome unload(final PalletUnloadCommand command);
}
