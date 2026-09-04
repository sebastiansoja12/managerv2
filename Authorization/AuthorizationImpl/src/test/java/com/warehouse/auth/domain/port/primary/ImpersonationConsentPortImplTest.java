package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.exception.ImpersonationConsentNotFoundException;
import com.warehouse.auth.domain.exception.ImpersonationConsentRequiredException;
import com.warehouse.auth.domain.model.ImpersonationConsent;
import com.warehouse.auth.domain.port.secondary.ImpersonationConsentRepository;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImpersonationConsentPortImplTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    private static final PlatformOwnerId OWNER = new PlatformOwnerId(1L);

    private static final OperatorId OPERATOR = OperatorId.of(20L);

    private static final UserId SUPERADMIN = new UserId(5L);

    private final InMemoryRepository repository = new InMemoryRepository();

    private final List<OperatorId> notifications = new ArrayList<>();

    private final ImpersonationConsentPort port = new ImpersonationConsentPortImpl(repository, notifications::add,
            Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void shouldNotifyOperatorOnlyWhenConsentsChange() {
        final ImpersonationConsent consent = port.request(OWNER, "owner", OPERATOR, "Problem");
        port.request(OWNER, "owner", OPERATOR, "Ponownie");
        port.findPending(OPERATOR);
        port.findActive(OPERATOR);
        port.approve(consent.getId(), OPERATOR, SUPERADMIN, "superadmin", 60);
        port.revoke(consent.getId(), OPERATOR, SUPERADMIN, "superadmin");

        assertThat(notifications).containsExactly(OPERATOR, OPERATOR, OPERATOR);
    }

    @Test
    void shouldReuseOpenRequestInsteadOfCreatingDuplicate() {
        final ImpersonationConsent first = port.request(OWNER, "owner", OPERATOR, "Problem");

        final ImpersonationConsent second = port.request(OWNER, "owner", OPERATOR, "Inny powód");

        assertThat(second.getId()).isEqualTo(first.getId());
        assertThat(port.findPending(OPERATOR)).hasSize(1);
    }

    @Test
    void shouldRequireApprovedConsentToImpersonate() {
        assertThat(assertThrows(ImpersonationConsentRequiredException.class,
                () -> port.requireGrant(OWNER, OPERATOR)).getReason()).isEqualTo("NONE");

        final ImpersonationConsent consent = port.request(OWNER, "owner", OPERATOR, null);
        assertThat(assertThrows(ImpersonationConsentRequiredException.class,
                () -> port.requireGrant(OWNER, OPERATOR)).getReason()).isEqualTo("PENDING");

        port.approve(consent.getId(), OPERATOR, SUPERADMIN, "superadmin", 60);
        assertThat(port.requireGrant(OWNER, OPERATOR).getId()).isEqualTo(consent.getId());
        assertThat(port.isGranted(consent.getId(), OWNER, OPERATOR)).isTrue();
    }

    @Test
    void shouldReportRejectedConsent() {
        final ImpersonationConsent consent = port.request(OWNER, "owner", OPERATOR, null);
        port.reject(consent.getId(), OPERATOR, SUPERADMIN, "superadmin");

        final ImpersonationConsentRequiredException exception = assertThrows(ImpersonationConsentRequiredException.class,
                () -> port.requireGrant(OWNER, OPERATOR));

        assertThat(exception.getReason()).isEqualTo("REJECTED");
    }

    @Test
    void shouldNotLetOtherOperatorDecide() {
        final ImpersonationConsent consent = port.request(OWNER, "owner", OPERATOR, null);

        assertThrows(ImpersonationConsentNotFoundException.class,
                () -> port.approve(consent.getId(), OperatorId.of(21L), SUPERADMIN, "superadmin", 60));
    }

    @Test
    void shouldStopGrantingAfterRevocation() {
        final ImpersonationConsent consent = port.request(OWNER, "owner", OPERATOR, null);
        port.approve(consent.getId(), OPERATOR, SUPERADMIN, "superadmin", 60);
        assertThat(port.findActive(OPERATOR)).hasSize(1);

        port.revoke(consent.getId(), OPERATOR, SUPERADMIN, "superadmin");

        assertThat(port.isGranted(consent.getId(), OWNER, OPERATOR)).isFalse();
        assertThat(port.findActive(OPERATOR)).isEmpty();
    }

    @Test
    void shouldNotGrantConsentOfAnotherOwner() {
        final ImpersonationConsent consent = port.request(OWNER, "owner", OPERATOR, null);
        port.approve(consent.getId(), OPERATOR, SUPERADMIN, "superadmin", 60);

        assertThat(port.isGranted(consent.getId(), new PlatformOwnerId(2L), OPERATOR)).isFalse();
        assertThat(port.isGranted(null, OWNER, OPERATOR)).isFalse();
    }

    private static final class InMemoryRepository implements ImpersonationConsentRepository {

        private final Map<Long, ImpersonationConsent> consents = new HashMap<>();

        private long sequence;

        @Override
        public ImpersonationConsent save(final ImpersonationConsent consent) {
            final Long id = consent.getId() != null ? consent.getId() : ++sequence;
            final ImpersonationConsent stored = ImpersonationConsent.restore(id, consent.getOperatorId(),
                    consent.getPlatformOwnerId(), consent.getPlatformOwnerUsername(), consent.getReason(),
                    consent.getStatus(), consent.getRequestedAt(), consent.getRequestExpiresAt(), consent.getDecidedBy(),
                    consent.getDecidedByUsername(), consent.getDecidedAt(), consent.getAccessExpiresAt());
            consents.put(id, stored);
            return stored;
        }

        @Override
        public Optional<ImpersonationConsent> findById(final Long consentId) {
            return Optional.ofNullable(consents.get(consentId));
        }

        @Override
        public Optional<ImpersonationConsent> findLatest(final PlatformOwnerId platformOwnerId, final OperatorId operatorId) {
            return consents.values().stream()
                    .filter(consent -> consent.belongsTo(platformOwnerId, operatorId))
                    .max(Comparator.comparing(ImpersonationConsent::getId));
        }

        @Override
        public List<ImpersonationConsent> findRecent(final OperatorId operatorId) {
            return new ArrayList<>(consents.values().stream()
                    .filter(consent -> consent.getOperatorId().equals(operatorId))
                    .toList());
        }
    }
}
