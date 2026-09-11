package com.warehouse.logistics.application.port.primary.command;

import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentHandoverCommand(ProcessId processId,
                                      List<TrackingNumber> trackingNumbers,
                                      String recipientName,
                                      String signature) {
}
