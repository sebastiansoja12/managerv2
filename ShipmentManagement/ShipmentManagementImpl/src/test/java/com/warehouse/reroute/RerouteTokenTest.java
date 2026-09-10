package com.warehouse.reroute;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.domain.enumeration.RerouteTokenVerification;
import com.warehouse.reroute.domain.exception.InvalidRerouteTokenException;
import com.warehouse.reroute.domain.exception.RerouteTokenExpiredException;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.vo.RerouteTokenPolicy;
import com.warehouse.reroute.domain.vo.RerouteTokenValue;

class RerouteTokenTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    private static final RerouteTokenPolicy POLICY = new RerouteTokenPolicy(Duration.ofMinutes(30), 3);

    private final RerouteToken rerouteToken = RerouteToken.issue(new ShipmentId(1L), "client@example.com",
            new RerouteTokenValue(42), NOW, POLICY);

    @Test
    void shouldAcceptMatchingTokenWithinValidity() {
        final RerouteTokenVerification verification = rerouteToken.verify(new RerouteTokenValue(42),
                NOW.plusSeconds(60), POLICY);

        assertEquals(RerouteTokenVerification.VALID, verification);
    }

    @Test
    void shouldRejectTokenAfterExpiry() {
        final RerouteTokenVerification verification = rerouteToken.verify(new RerouteTokenValue(42),
                NOW.plus(Duration.ofMinutes(31)), POLICY);

        assertEquals(RerouteTokenVerification.EXPIRED, verification);
    }

    @Test
    void shouldBlockTokenAfterTooManyInvalidAttempts() {
        rerouteToken.verify(new RerouteTokenValue(1), NOW, POLICY);
        rerouteToken.verify(new RerouteTokenValue(2), NOW, POLICY);

        final RerouteTokenVerification verification = rerouteToken.verify(new RerouteTokenValue(3), NOW, POLICY);

        assertAll(
                () -> assertEquals(RerouteTokenVerification.ATTEMPTS_EXCEEDED, verification),
                () -> assertFalse(rerouteToken.isActive(NOW.plusSeconds(1))),
                () -> assertEquals(RerouteTokenVerification.EXPIRED,
                        rerouteToken.verify(new RerouteTokenValue(42), NOW.plusSeconds(1), POLICY))
        );
    }

    @Test
    void shouldNotAllowTokenToBeUsedTwice() {
        rerouteToken.markUsed(NOW);

        assertAll(
                () -> assertFalse(rerouteToken.isActive(NOW)),
                () -> assertThrows(RerouteTokenExpiredException.class, () -> rerouteToken.markUsed(NOW))
        );
    }

    @Test
    void shouldFormatAndParseSixDigitTokens() {
        final RerouteTokenValue value = RerouteTokenValue.parse(" 004200 ");

        assertAll(
                () -> assertEquals(4200, value.value()),
                () -> assertEquals("004200", value.formatted()),
                () -> assertThrows(InvalidRerouteTokenException.class, () -> RerouteTokenValue.parse("12a456")),
                () -> assertThrows(InvalidRerouteTokenException.class, () -> RerouteTokenValue.parse("12345")),
                () -> assertTrue(rerouteToken.isActive(NOW))
        );
    }
}
