package com.warehouse.deliverycollect.application.port.primary.command;

import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record DeliveryCollectCommand(ProcessId processId, List<TrackingNumber> trackingNumbers) {
}
