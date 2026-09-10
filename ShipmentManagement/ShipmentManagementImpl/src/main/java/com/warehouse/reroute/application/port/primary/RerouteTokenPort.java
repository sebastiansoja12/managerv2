package com.warehouse.reroute.application.port.primary;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.reroute.application.port.primary.command.RerouteShipmentCommand;
import com.warehouse.reroute.application.port.primary.command.VerifyRerouteTokenCommand;
import com.warehouse.reroute.application.port.primary.result.ReroutableShipmentDetails;
import com.warehouse.reroute.application.port.primary.result.ReroutableShipmentSummary;
import com.warehouse.reroute.application.port.primary.result.RerouteRequestAccepted;
import com.warehouse.reroute.application.port.primary.result.RerouteTokenIssued;

public interface RerouteTokenPort {

    ReroutableShipmentSummary findShipment(final TrackingNumber trackingNumber);

    RerouteTokenIssued requestToken(final TrackingNumber trackingNumber);

    ReroutableShipmentDetails verifyToken(final VerifyRerouteTokenCommand command);

    RerouteRequestAccepted reroute(final RerouteShipmentCommand command);

    void removeExpiredTokens();
}
