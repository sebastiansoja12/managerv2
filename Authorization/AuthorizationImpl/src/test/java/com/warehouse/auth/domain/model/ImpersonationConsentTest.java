package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImpersonationConsentTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    private static final UserId SUPERADMIN = new UserId(5L);

    private final ImpersonationConsent request = ImpersonationConsent.request(OperatorId.of(20L), new PlatformOwnerId(1L),
            "owner", "  Problem z etykietami  ", NOW);

    @Test
    void shouldStartAsPendingRequestWithoutAccess() {
        assertThat(request.statusAt(NOW)).isEqualTo(ImpersonationConsentStatus.PENDING);
        assertThat(request.grantsAccess(NOW)).isFalse();
        assertThat(request.getReason()).isEqualTo("Problem z etykietami");
    }

    @Test
    void shouldExpireUnansweredRequestAfterDay() {
        assertThat(request.statusAt(NOW.plus(Duration.ofHours(24)))).isEqualTo(ImpersonationConsentStatus.EXPIRED);
        assertThrows(IllegalStateException.class,
                () -> request.approve(SUPERADMIN, "superadmin", 60, NOW.plus(Duration.ofHours(25))));
    }

    @Test
    void shouldGrantAccessForApprovedDuration() {
        final ImpersonationConsent approved = request.approve(SUPERADMIN, "superadmin", 60, NOW);

        assertThat(approved.grantsAccess(NOW.plus(Duration.ofMinutes(59)))).isTrue();
        assertThat(approved.statusAt(NOW.plus(Duration.ofMinutes(60)))).isEqualTo(ImpersonationConsentStatus.EXPIRED);
        assertThat(approved.getDecidedByUsername()).isEqualTo("superadmin");
    }

    @Test
    void shouldRejectUnsupportedDuration() {
        assertThrows(IllegalArgumentException.class, () -> request.approve(SUPERADMIN, "superadmin", 45, NOW));
    }

    @Test
    void shouldNotGrantAccessAfterRejection() {
        final ImpersonationConsent rejected = request.reject(SUPERADMIN, "superadmin", NOW);

        assertThat(rejected.statusAt(NOW)).isEqualTo(ImpersonationConsentStatus.REJECTED);
        assertThat(rejected.grantsAccess(NOW)).isFalse();
        assertThrows(IllegalStateException.class, () -> rejected.approve(SUPERADMIN, "superadmin", 60, NOW));
    }

    @Test
    void shouldEndAccessWhenRevoked() {
        final ImpersonationConsent revoked = request.approve(SUPERADMIN, "superadmin", 60, NOW)
                .revoke(SUPERADMIN, "superadmin", NOW.plus(Duration.ofMinutes(5)));

        assertThat(revoked.statusAt(NOW.plus(Duration.ofMinutes(6)))).isEqualTo(ImpersonationConsentStatus.REVOKED);
        assertThat(revoked.grantsAccess(NOW.plus(Duration.ofMinutes(6)))).isFalse();
    }

    @Test
    void shouldNotRevokePendingRequest() {
        assertThrows(IllegalStateException.class, () -> request.revoke(SUPERADMIN, "superadmin", NOW));
    }
}
