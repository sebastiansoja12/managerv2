package com.warehouse.shipment.application.port.primary.command;

import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentHandoverCommand(TrackingNumber trackingNumber, String recipientName, String signature) {
}
