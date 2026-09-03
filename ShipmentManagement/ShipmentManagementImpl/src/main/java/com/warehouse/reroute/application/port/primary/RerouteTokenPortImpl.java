package com.warehouse.reroute.application.port.primary;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.reroute.application.port.primary.command.RerouteShipmentCommand;
import com.warehouse.reroute.application.port.primary.command.VerifyRerouteTokenCommand;
import com.warehouse.reroute.application.port.primary.result.ReroutableShipmentDetails;
import com.warehouse.reroute.application.port.primary.result.ReroutableShipmentSummary;
import com.warehouse.reroute.application.port.primary.result.RerouteRequestAccepted;
import com.warehouse.reroute.application.port.primary.result.RerouteTokenIssued;
import com.warehouse.reroute.application.port.secondary.RerouteIntegrationEventServicePort;
import com.warehouse.reroute.application.port.secondary.RerouteNotificationServicePort;
import com.warehouse.reroute.application.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.application.port.secondary.ShipmentRerouteServicePort;
import com.warehouse.reroute.domain.enumeration.RerouteTokenVerification;
import com.warehouse.reroute.domain.exception.*;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.service.RerouteTokenValueGenerator;
import com.warehouse.reroute.domain.vo.ReroutableShipment;
import com.warehouse.reroute.domain.vo.RerouteTokenPolicy;
import com.warehouse.reroute.domain.vo.RerouteTokenValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Slf4j
public class RerouteTokenPortImpl implements RerouteTokenPort {

    private final RerouteTokenRepository rerouteTokenRepository;

    private final ShipmentRerouteServicePort shipmentRerouteServicePort;

    private final RerouteNotificationServicePort rerouteNotificationServicePort;

    private final RerouteIntegrationEventServicePort rerouteIntegrationEventServicePort;

    private final RerouteTokenValueGenerator rerouteTokenValueGenerator;

    private final RerouteTokenPolicy rerouteTokenPolicy;

    private final Clock clock;

    public RerouteTokenPortImpl(final RerouteTokenRepository rerouteTokenRepository,
                                final ShipmentRerouteServicePort shipmentRerouteServicePort,
                                final RerouteNotificationServicePort rerouteNotificationServicePort,
                                final RerouteIntegrationEventServicePort rerouteIntegrationEventServicePort,
                                final RerouteTokenValueGenerator rerouteTokenValueGenerator,
                                final RerouteTokenPolicy rerouteTokenPolicy,
                                final Clock clock) {
        this.rerouteTokenRepository = rerouteTokenRepository;
        this.shipmentRerouteServicePort = shipmentRerouteServicePort;
        this.rerouteNotificationServicePort = rerouteNotificationServicePort;
        this.rerouteIntegrationEventServicePort = rerouteIntegrationEventServicePort;
        this.rerouteTokenValueGenerator = rerouteTokenValueGenerator;
        this.rerouteTokenPolicy = rerouteTokenPolicy;
        this.clock = clock;
    }

    @Override
    public ReroutableShipmentSummary findShipment(final TrackingNumber trackingNumber) {
        final ReroutableShipment shipment = loadShipment(trackingNumber);
        return new ReroutableShipmentSummary(shipment.trackingNumber(), shipment.status(), shipment.reroutable(),
                shipment.maskedNotificationEmail());
    }

    @Override
    public RerouteTokenIssued requestToken(final TrackingNumber trackingNumber) {
        final ReroutableShipment shipment = loadReroutableShipment(trackingNumber);
        final String email = shipment.notificationEmail();
        if (email == null) {
            throw new MissingRerouteEmailException();
        }
        final Instant now = clock.instant();

        rerouteTokenRepository.findActiveByShipmentId(shipment.shipmentId(), now)
                .forEach(activeToken -> {
                    activeToken.revoke(now);
                    rerouteTokenRepository.save(activeToken);
                });

        final RerouteToken rerouteToken = rerouteTokenRepository.save(RerouteToken.issue(
                shipment.shipmentId(), email, rerouteTokenValueGenerator.generate(), now, rerouteTokenPolicy));
        rerouteNotificationServicePort.sendRerouteToken(shipment, rerouteToken);
        log.info("Reroute token issued for shipment {}", shipment.shipmentId().getValue());
        return new RerouteTokenIssued(shipment.maskedNotificationEmail(), rerouteToken.getExpiresAt());
    }

    @Override
    public ReroutableShipmentDetails verifyToken(final VerifyRerouteTokenCommand command) {
        final ReroutableShipment shipment = loadReroutableShipment(command.trackingNumber());
        final RerouteToken rerouteToken = verifiedToken(shipment, command.token());
        return new ReroutableShipmentDetails(shipment, rerouteToken.getExpiresAt());
    }

    @Override
    @Transactional(noRollbackFor = {
            InvalidRerouteTokenException.class,
            RerouteTokenAttemptsExceededException.class,
            RerouteTokenExpiredException.class
    })
    public RerouteRequestAccepted reroute(final RerouteShipmentCommand command) {
        final ReroutableShipment shipment = loadReroutableShipment(command.trackingNumber());
        final RerouteToken rerouteToken = verifiedToken(shipment, command.token());
        final Instant now = clock.instant();

        rerouteToken.markUsed(now);
        this.rerouteTokenRepository.save(rerouteToken);
        this.rerouteIntegrationEventServicePort.publishRerouteRequested(
                shipment, rerouteToken, command.sender(), command.recipient(), now);
        log.info("Reroute of shipment {} requested by client", shipment.shipmentId().getValue());
        return new RerouteRequestAccepted(shipment.trackingNumber(), now);
    }

    @Override
    public void removeExpiredTokens() {
        rerouteTokenRepository.deleteExpiredBefore(clock.instant());
    }

    private RerouteToken verifiedToken(final ReroutableShipment shipment, final RerouteTokenValue providedToken) {
        final Instant now = clock.instant();
        final RerouteToken rerouteToken = rerouteTokenRepository.findLatestByShipmentId(shipment.shipmentId())
                .orElseThrow(RerouteTokenExpiredException::new);
        final RerouteTokenVerification verification = rerouteToken.verify(providedToken, now, rerouteTokenPolicy);
        if (verification == RerouteTokenVerification.VALID) {
            return rerouteToken;
        }
        if (verification == RerouteTokenVerification.EXPIRED) {
            throw new RerouteTokenExpiredException();
        }
        rerouteTokenRepository.save(rerouteToken);
        log.warn("Invalid reroute token provided for shipment {}", shipment.shipmentId().getValue());
        if (verification == RerouteTokenVerification.ATTEMPTS_EXCEEDED) {
            throw new RerouteTokenAttemptsExceededException();
        }
        throw new InvalidRerouteTokenException();
    }

    private ReroutableShipment loadReroutableShipment(final TrackingNumber trackingNumber) {
        final ReroutableShipment shipment = loadShipment(trackingNumber);
        if (!shipment.reroutable()) {
            throw new ShipmentNotReroutableException();
        }
        return shipment;
    }

    private ReroutableShipment loadShipment(final TrackingNumber trackingNumber) {
        return shipmentRerouteServicePort.findShipment(trackingNumber)
                .orElseThrow(RerouteShipmentNotFoundException::new);
    }
}
