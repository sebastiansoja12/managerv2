package com.warehouse.reroute.infrastructure.adapter.primary.mapper;


import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.reroute.application.port.primary.command.RerouteShipmentCommand;
import com.warehouse.reroute.application.port.primary.command.VerifyRerouteTokenCommand;
import com.warehouse.reroute.application.port.primary.result.ReroutableShipmentDetails;
import com.warehouse.reroute.application.port.primary.result.ReroutableShipmentSummary;
import com.warehouse.reroute.application.port.primary.result.RerouteRequestAccepted;
import com.warehouse.reroute.application.port.primary.result.RerouteTokenIssued;
import com.warehouse.reroute.domain.vo.ReroutableShipment;
import com.warehouse.reroute.domain.vo.RerouteParty;
import com.warehouse.reroute.domain.vo.RerouteTokenValue;
import com.warehouse.reroute.infrastructure.adapter.primary.api.ReroutableShipmentApiResponse;
import com.warehouse.reroute.infrastructure.adapter.primary.api.RerouteAcceptedApiResponse;
import com.warehouse.reroute.infrastructure.adapter.primary.api.ReroutableShipmentSummaryApiResponse;
import com.warehouse.reroute.infrastructure.adapter.primary.api.ReroutePartyApi;
import com.warehouse.reroute.infrastructure.adapter.primary.api.RerouteShipmentApiRequest;
import com.warehouse.reroute.infrastructure.adapter.primary.api.RerouteTokenIssuedApiResponse;
import com.warehouse.reroute.infrastructure.adapter.primary.api.RerouteTokenVerificationApiRequest;

public class RerouteApiMapper {

    public TrackingNumber mapTrackingNumber(final String trackingNumber) {
        return new TrackingNumber(trackingNumber.trim());
    }

    public VerifyRerouteTokenCommand map(final String trackingNumber, final RerouteTokenVerificationApiRequest request) {
        return new VerifyRerouteTokenCommand(mapTrackingNumber(trackingNumber), RerouteTokenValue.parse(request.token()));
    }

    public RerouteShipmentCommand map(final String trackingNumber, final RerouteShipmentApiRequest request) {
        return new RerouteShipmentCommand(
                mapTrackingNumber(trackingNumber),
                RerouteTokenValue.parse(request.token()),
                map(request.sender()),
                map(request.recipient()));
    }

    public ReroutableShipmentSummaryApiResponse map(final ReroutableShipmentSummary summary) {
        return new ReroutableShipmentSummaryApiResponse(
                summary.trackingNumber().value(),
                summary.status().name(),
                summary.reroutable(),
                summary.maskedEmail());
    }

    public RerouteTokenIssuedApiResponse map(final RerouteTokenIssued rerouteTokenIssued) {
        return new RerouteTokenIssuedApiResponse(rerouteTokenIssued.maskedEmail(), rerouteTokenIssued.expiresAt());
    }

    public ReroutableShipmentApiResponse map(final ReroutableShipmentDetails details) {
        final ReroutableShipment shipment = details.shipment();
        return new ReroutableShipmentApiResponse(
                shipment.trackingNumber().value(),
                shipment.status().name(),
                shipment.reroutable(),
                map(shipment.sender()),
                map(shipment.recipient()),
                details.tokenExpiresAt());
    }

    public RerouteAcceptedApiResponse map(final RerouteRequestAccepted rerouteRequestAccepted) {
        return new RerouteAcceptedApiResponse(
                rerouteRequestAccepted.trackingNumber().value(),
                rerouteRequestAccepted.requestedAt());
    }

    private RerouteParty map(final ReroutePartyApi party) {
        return new RerouteParty(
                party.firstName().trim(),
                party.lastName().trim(),
                party.email().trim(),
                party.telephoneNumber().trim(),
                party.city().trim(),
                party.postalCode().trim(),
                party.street().trim(),
                null);
    }

    private ReroutePartyApi map(final RerouteParty party) {
        return new ReroutePartyApi(party.firstName(), party.lastName(), party.email(), party.telephoneNumber(),
                party.city(), party.postalCode(), party.street(), party.countryCode());
    }
}
