package com.warehouse.reroute.application.port.primary.command;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.reroute.domain.vo.RerouteParty;
import com.warehouse.reroute.domain.vo.RerouteTokenValue;

public record RerouteShipmentCommand(TrackingNumber trackingNumber,
                                     RerouteTokenValue token,
                                     RerouteParty sender,
                                     RerouteParty recipient) {
}
