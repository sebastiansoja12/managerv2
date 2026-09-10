package com.warehouse.shipment.application.port.primary.command;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.vo.Party;

public record ShipmentRerouteCommand(TrackingNumber trackingNumber, Party sender, Party recipient) {
}
