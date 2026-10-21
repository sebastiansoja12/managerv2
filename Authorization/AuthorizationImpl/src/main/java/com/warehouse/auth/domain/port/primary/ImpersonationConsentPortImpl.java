package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.exception.ImpersonationConsentNotFoundException;
import com.warehouse.auth.domain.exception.ImpersonationConsentRequiredException;
import com.warehouse.auth.domain.model.ImpersonationConsent;
import com.warehouse.auth.domain.port.secondary.ImpersonationConsentNotificationServicePort;
import com.warehouse.auth.domain.port.secondary.ImpersonationConsentRepository;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ImpersonationConsentPortImpl implements ImpersonationConsentPort {

    private final ImpersonationConsentRepository repository;

    private final ImpersonationConsentNotificationServicePort notificationServicePort;

    private final Clock clock;

    public ImpersonationConsentPortImpl(final ImpersonationConsentRepository repository,
                                        final ImpersonationConsentNotificationServicePort notificationServicePort,
                                        final Clock clock) {
        this.repository = repository;
        this.notificationServicePort = notificationServicePort;
        this.clock = clock;
    }

    @Override
    public ImpersonationConsent request(final PlatformOwnerId platformOwnerId,
                                        final String platformOwnerUsername,
                                        final OperatorId operatorId,
                                        final String reason) {
        final Instant now = now();
        final Optional<ImpersonationConsent> open = repository.findLatest(platformOwnerId, operatorId)
                .filter(consent -> consent.isOpen(now));
        if (open.isPresent()) {
            return open.get();
        }
        final ImpersonationConsent consent = repository.save(ImpersonationConsent.request(operatorId, platformOwnerId,
                platformOwnerUsername, reason, now));
        log.info("Platform owner {} requested impersonation consent from operator {}", platformOwnerId.value(),
                operatorId.value());
        notificationServicePort.notifyConsentsChanged(operatorId);
        return consent;
    }

    @Override
    public Optional<ImpersonationConsent> findLatest(final PlatformOwnerId platformOwnerId, final OperatorId operatorId) {
        return repository.findLatest(platformOwnerId, operatorId);
    }

    @Override
    public List<ImpersonationConsent> findPending(final OperatorId operatorId) {
        final Instant now = now();
        return repository.findRecent(operatorId)
                .stream()
                .filter(consent -> consent.statusAt(now) == com.warehouse.auth.domain.model.ImpersonationConsentStatus.PENDING)
                .toList();
    }

    @Override
    public List<ImpersonationConsent> findActive(final OperatorId operatorId) {
        final Instant now = now();
        return repository.findRecent(operatorId)
                .stream()
                .filter(consent -> consent.grantsAccess(now))
                .toList();
    }

    @Override
    public ImpersonationConsent approve(final Long consentId,
                                        final OperatorId operatorId,
                                        final UserId userId,
                                        final String username,
                                        final int durationMinutes) {
        final ImpersonationConsent consent = repository.save(load(consentId, operatorId)
                .approve(userId, username, durationMinutes, now()));
        log.info("User {} approved impersonation consent {} for {} minutes", userId.value(), consentId, durationMinutes);
        notificationServicePort.notifyConsentsChanged(operatorId);
        return consent;
    }

    @Override
    public ImpersonationConsent reject(final Long consentId,
                                       final OperatorId operatorId,
                                       final UserId userId,
                                       final String username) {
        final ImpersonationConsent consent = repository.save(load(consentId, operatorId).reject(userId, username, now()));
        log.info("User {} rejected impersonation consent {}", userId.value(), consentId);
        notificationServicePort.notifyConsentsChanged(operatorId);
        return consent;
    }

    @Override
    public ImpersonationConsent revoke(final Long consentId,
                                       final OperatorId operatorId,
                                       final UserId userId,
                                       final String username) {
        final ImpersonationConsent consent = repository.save(load(consentId, operatorId).revoke(userId, username, now()));
        log.info("User {} revoked impersonation consent {}", userId.value(), consentId);
        notificationServicePort.notifyConsentsChanged(operatorId);
        return consent;
    }

    @Override
    public ImpersonationConsent requireGrant(final PlatformOwnerId platformOwnerId, final OperatorId operatorId) {
        final Instant now = now();
        final ImpersonationConsent consent = repository.findLatest(platformOwnerId, operatorId)
                .orElseThrow(() -> new ImpersonationConsentRequiredException(ImpersonationConsentRequiredException.NONE));
        if (!consent.grantsAccess(now)) {
            throw new ImpersonationConsentRequiredException(consent.statusAt(now).name());
        }
        return consent;
    }

    @Override
    public boolean isGranted(final Long consentId, final PlatformOwnerId platformOwnerId, final OperatorId operatorId) {
        if (consentId == null || platformOwnerId == null || operatorId == null) {
            return false;
        }
        final Instant now = now();
        return repository.findById(consentId)
                .filter(consent -> consent.belongsTo(platformOwnerId, operatorId))
                .filter(consent -> consent.grantsAccess(now))
                .isPresent();
    }

    @Override
    public Instant now() {
        return Instant.now(clock);
    }

    private ImpersonationConsent load(final Long consentId, final OperatorId operatorId) {
        return repository.findById(consentId)
                .filter(consent -> consent.getOperatorId().equals(operatorId))
                .orElseThrow(() -> new ImpersonationConsentNotFoundException(consentId));
    }
}
