package com.warehouse.reroute;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.reroute.application.port.primary.RerouteTokenPortImpl;
import com.warehouse.reroute.application.port.primary.command.RerouteShipmentCommand;
import com.warehouse.reroute.application.port.primary.command.VerifyRerouteTokenCommand;
import com.warehouse.reroute.application.port.primary.result.RerouteRequestAccepted;
import com.warehouse.reroute.application.port.primary.result.RerouteTokenIssued;
import com.warehouse.reroute.application.port.secondary.RerouteIntegrationEventServicePort;
import com.warehouse.reroute.application.port.secondary.RerouteNotificationServicePort;
import com.warehouse.reroute.application.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.application.port.secondary.ShipmentRerouteServicePort;
import com.warehouse.reroute.domain.exception.InvalidRerouteTokenException;
import com.warehouse.reroute.domain.exception.ShipmentNotReroutableException;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.service.RerouteTokenValueGenerator;
import com.warehouse.reroute.domain.vo.ReroutableShipment;
import com.warehouse.reroute.domain.vo.RerouteParty;
import com.warehouse.reroute.domain.vo.RerouteTokenId;
import com.warehouse.reroute.domain.vo.RerouteTokenPolicy;
import com.warehouse.reroute.domain.vo.RerouteTokenValue;

@ExtendWith(MockitoExtension.class)
class RerouteTokenPortImplTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    private static final RerouteTokenPolicy POLICY = new RerouteTokenPolicy(Duration.ofMinutes(30), 5);

    private static final TrackingNumber TRACKING_NUMBER = new TrackingNumber("TRK-001");

    private static final ShipmentId SHIPMENT_ID = new ShipmentId(10L);

    @Mock
    private RerouteTokenRepository rerouteTokenRepository;

    @Mock
    private ShipmentRerouteServicePort shipmentRerouteServicePort;

    @Mock
    private RerouteNotificationServicePort rerouteNotificationServicePort;

    @Mock
    private RerouteIntegrationEventServicePort rerouteIntegrationEventServicePort;

    @Mock
    private RerouteTokenValueGenerator rerouteTokenValueGenerator;

    private RerouteTokenPortImpl rerouteTokenPort;

    @BeforeEach
    void setUp() {
        rerouteTokenPort = new RerouteTokenPortImpl(rerouteTokenRepository, shipmentRerouteServicePort,
                rerouteNotificationServicePort, rerouteIntegrationEventServicePort, rerouteTokenValueGenerator,
                POLICY, Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    void shouldIssueTokenForSenderEmailAndRevokePreviousToken() {
        final RerouteToken previousToken = activeToken(111111);
        when(shipmentRerouteServicePort.findShipment(TRACKING_NUMBER)).thenReturn(Optional.of(shipment(true)));
        when(rerouteTokenRepository.findActiveByShipmentId(SHIPMENT_ID, NOW)).thenReturn(List.of(previousToken));
        when(rerouteTokenValueGenerator.generate()).thenReturn(new RerouteTokenValue(123456));
        when(rerouteTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        final RerouteTokenIssued issued = rerouteTokenPort.requestToken(TRACKING_NUMBER);

        final ArgumentCaptor<RerouteToken> sentToken = ArgumentCaptor.forClass(RerouteToken.class);
        verify(rerouteNotificationServicePort).sendRerouteToken(any(), sentToken.capture());
        assertAll(
                () -> assertEquals("s***@example.com", issued.maskedEmail()),
                () -> assertEquals(NOW.plus(Duration.ofMinutes(30)), issued.expiresAt()),
                () -> assertEquals("sender@example.com", sentToken.getValue().getEmail()),
                () -> assertEquals(new RerouteTokenValue(123456), sentToken.getValue().getValue()),
                () -> assertEquals(NOW, previousToken.getExpiresAt())
        );
    }

    @Test
    void shouldNotIssueTokenWhenShipmentCannotBeChangedAnymore() {
        when(shipmentRerouteServicePort.findShipment(TRACKING_NUMBER)).thenReturn(Optional.of(shipment(false)));

        assertThrows(ShipmentNotReroutableException.class, () -> rerouteTokenPort.requestToken(TRACKING_NUMBER));
        verify(rerouteNotificationServicePort, never()).sendRerouteToken(any(), any());
    }

    @Test
    void shouldPersistFailedAttemptWhenTokenIsInvalid() {
        final RerouteToken token = activeToken(123456);
        when(shipmentRerouteServicePort.findShipment(TRACKING_NUMBER)).thenReturn(Optional.of(shipment(true)));
        when(rerouteTokenRepository.findLatestByShipmentId(SHIPMENT_ID)).thenReturn(Optional.of(token));

        assertThrows(InvalidRerouteTokenException.class, () -> rerouteTokenPort.verifyToken(
                new VerifyRerouteTokenCommand(TRACKING_NUMBER, new RerouteTokenValue(654321))));

        assertEquals(1, token.getFailedAttempts());
        verify(rerouteTokenRepository).save(token);
    }

    @Test
    void shouldRerouteShipmentAndConsumeToken() {
        final RerouteToken token = activeToken(123456);
        final RerouteParty sender = party("new-sender@example.com");
        final RerouteParty recipient = party("recipient@example.com");
        final ReroutableShipment shipment = shipment(true);
        when(shipmentRerouteServicePort.findShipment(TRACKING_NUMBER)).thenReturn(Optional.of(shipment));
        when(rerouteTokenRepository.findLatestByShipmentId(SHIPMENT_ID)).thenReturn(Optional.of(token));

        final RerouteRequestAccepted accepted = rerouteTokenPort.reroute(new RerouteShipmentCommand(
                TRACKING_NUMBER, new RerouteTokenValue(123456), sender, recipient));

        assertAll(
                () -> assertEquals(TRACKING_NUMBER, accepted.trackingNumber()),
                () -> assertEquals(NOW, accepted.requestedAt()),
                () -> assertEquals(NOW, token.getUsedAt()));
        verify(rerouteTokenRepository).save(token);
        verify(rerouteIntegrationEventServicePort).publishRerouteRequested(shipment, token, sender, recipient, NOW);
    }

    @Test
    void shouldNotPublishRerouteWhenTokenIsInvalid() {
        final RerouteToken token = activeToken(123456);
        final RerouteParty sender = party("new-sender@example.com");
        final RerouteParty recipient = party("recipient@example.com");
        when(shipmentRerouteServicePort.findShipment(TRACKING_NUMBER)).thenReturn(Optional.of(shipment(true)));
        when(rerouteTokenRepository.findLatestByShipmentId(SHIPMENT_ID)).thenReturn(Optional.of(token));

        assertThrows(InvalidRerouteTokenException.class, () -> rerouteTokenPort.reroute(new RerouteShipmentCommand(
                TRACKING_NUMBER, new RerouteTokenValue(654321), sender, recipient)));

        verify(rerouteIntegrationEventServicePort, never())
                .publishRerouteRequested(any(), any(), any(), any(), any());
    }

    private RerouteToken activeToken(final int value) {
        return new RerouteToken(new RerouteTokenId(1L), SHIPMENT_ID, new RerouteTokenValue(value),
                "sender@example.com", NOW.minusSeconds(60), NOW.plus(Duration.ofMinutes(10)), 0, null);
    }

    private ReroutableShipment shipment(final boolean reroutable) {
        return new ReroutableShipment(SHIPMENT_ID, TRACKING_NUMBER, ShipmentStatus.CREATED, reroutable,
                party("sender@example.com"), party("recipient@example.com"));
    }

    private RerouteParty party(final String email) {
        return new RerouteParty("Jan", "Kowalski", email, "500100200", "Katowice", "40-001", "Mariacka 1", "PL");
    }
}
