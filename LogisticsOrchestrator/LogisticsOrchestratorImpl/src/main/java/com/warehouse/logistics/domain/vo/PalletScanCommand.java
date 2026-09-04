package com.warehouse.logistics.domain.vo;

import com.warehouse.commonassets.identificator.ProcessId;

public record PalletScanCommand(ProcessId processId, String trackingNumber) {
}
