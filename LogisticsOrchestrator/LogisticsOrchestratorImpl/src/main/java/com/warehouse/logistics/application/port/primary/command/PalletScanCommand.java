package com.warehouse.logistics.application.port.primary.command;

import com.warehouse.commonassets.identificator.ProcessId;

public record PalletScanCommand(ProcessId processId, String trackingNumber) {
}
