package com.warehouse.account.domain.model;

import com.warehouse.account.domain.enumeration.UserInvitationFailure;
import com.warehouse.account.domain.exception.InvalidUserInvitationException;
import com.warehouse.account.domain.vo.InvitationToken;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserInvitationTest {

    private static final Instant ISSUED_AT = Instant.parse("2026-10-22T08:00:00Z");

    @Test
    void shouldAcceptInvitationWithStrongEnoughPassword() {
        final UserInvitation invitation = invitation();

        invitation.accept("Secret123", ISSUED_AT.plus(Duration.ofHours(1)));

        assertEquals(ISSUED_AT.plus(Duration.ofHours(1)), invitation.getAcceptedAt());
    }

    @Test
    void shouldRejectExpiredInvitation() {
        final UserInvitation invitation = invitation();

        final InvalidUserInvitationException exception = assertThrows(InvalidUserInvitationException.class,
                () -> invitation.accept("Secret123", ISSUED_AT.plus(Duration.ofHours(72))));

        assertEquals(UserInvitationFailure.EXPIRED, exception.getFailure());
    }

    @Test
    void shouldRejectAlreadyAcceptedInvitation() {
        final UserInvitation invitation = invitation();
        invitation.accept("Secret123", ISSUED_AT.plusSeconds(60));

        final InvalidUserInvitationException exception = assertThrows(InvalidUserInvitationException.class,
                () -> invitation.accept("Another123", ISSUED_AT.plusSeconds(120)));

        assertEquals(UserInvitationFailure.ALREADY_ACCEPTED, exception.getFailure());
    }

    @Test
    void shouldRejectTooShortPassword() {
        final UserInvitation invitation = invitation();

        final InvalidUserInvitationException exception = assertThrows(InvalidUserInvitationException.class,
                () -> invitation.accept("short", ISSUED_AT.plusSeconds(60)));

        assertEquals(UserInvitationFailure.WEAK_PASSWORD, exception.getFailure());
    }

    @Test
    void shouldGenerateUniqueTokensAndStableHashes() {
        final InvitationToken first = InvitationToken.generate();
        final InvitationToken second = InvitationToken.generate();

        assertNotEquals(first.value(), second.value());
        assertEquals(first.hash(), new InvitationToken(first.value()).hash());
        assertEquals(64, first.hash().length());
    }

    private UserInvitation invitation() {
        return UserInvitation.issue(new UserId(101L), new SupplierId(11L), "KUR-01", "hash", ISSUED_AT,
                Duration.ofHours(72));
    }
}
