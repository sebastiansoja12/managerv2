package com.warehouse.reroute.application.port.primary.command;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.reroute.domain.vo.RerouteTokenValue;

public record VerifyRerouteTokenCommand(TrackingNumber trackingNumber, RerouteTokenValue token) {
}
