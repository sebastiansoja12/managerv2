package com.warehouse.logistics.application.port.primary.command;

import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentCollectCommand(ProcessId processId, List<TrackingNumber> trackingNumbers) {
}
