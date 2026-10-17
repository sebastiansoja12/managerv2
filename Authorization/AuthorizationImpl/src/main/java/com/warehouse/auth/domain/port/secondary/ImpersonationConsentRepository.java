package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.ImpersonationConsent;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;

import java.util.List;
import java.util.Optional;

public interface ImpersonationConsentRepository {

    ImpersonationConsent save(final ImpersonationConsent consent);

    Optional<ImpersonationConsent> findById(final Long consentId);

    Optional<ImpersonationConsent> findLatest(final PlatformOwnerId platformOwnerId, final OperatorId operatorId);

    List<ImpersonationConsent> findRecent(final OperatorId operatorId);
}
