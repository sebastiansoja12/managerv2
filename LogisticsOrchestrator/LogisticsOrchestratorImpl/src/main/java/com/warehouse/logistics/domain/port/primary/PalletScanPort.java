package com.warehouse.logistics.domain.port.primary;

import com.warehouse.logistics.domain.vo.PalletScanCommand;
import com.warehouse.logistics.domain.vo.PalletScanOutcome;

public interface PalletScanPort {
    PalletScanOutcome scan(final PalletScanCommand command);
}
