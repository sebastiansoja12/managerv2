package com.warehouse.logistics.application.port.primary.command;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.ProcessId;

public record PalletUnloadCommand(ProcessId processId, PalletTrackingNumber trackingNumber) {
}
