package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.ImpersonationConsent;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;
import java.util.Optional;

public interface ImpersonationConsentPort {

    ImpersonationConsent request(final PlatformOwnerId platformOwnerId,
                                 final String platformOwnerUsername,
                                 final OperatorId operatorId,
                                 final String reason);

    Optional<ImpersonationConsent> findLatest(final PlatformOwnerId platformOwnerId, final OperatorId operatorId);

    List<ImpersonationConsent> findPending(final OperatorId operatorId);

    List<ImpersonationConsent> findActive(final OperatorId operatorId);

    ImpersonationConsent approve(final Long consentId,
                                 final OperatorId operatorId,
                                 final UserId userId,
                                 final String username,
                                 final int durationMinutes);

    ImpersonationConsent reject(final Long consentId,
                                final OperatorId operatorId,
                                final UserId userId,
                                final String username);

    ImpersonationConsent revoke(final Long consentId,
                                final OperatorId operatorId,
                                final UserId userId,
                                final String username);

    ImpersonationConsent requireGrant(final PlatformOwnerId platformOwnerId, final OperatorId operatorId);

    boolean isGranted(final Long consentId, final PlatformOwnerId platformOwnerId, final OperatorId operatorId);

    java.time.Instant now();
}
