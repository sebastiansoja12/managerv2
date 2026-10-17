package com.warehouse.logistics.domain.vo;

import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentCollectCommand(ProcessId processId, List<TrackingNumber> trackingNumbers) {
}
