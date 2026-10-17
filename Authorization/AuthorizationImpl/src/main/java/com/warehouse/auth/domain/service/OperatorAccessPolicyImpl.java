package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.port.secondary.OperatorSessionRepository;
import com.warehouse.auth.domain.port.secondary.OperatorStatusServicePort;
import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class OperatorAccessPolicyImpl implements OperatorAccessPolicy {

    private final OperatorStatusServicePort operatorStatusServicePort;

    private final OperatorSessionRepository operatorSessionRepository;

    public OperatorAccessPolicyImpl(final OperatorStatusServicePort operatorStatusServicePort,
                                    final OperatorSessionRepository operatorSessionRepository) {
        this.operatorStatusServicePort = operatorStatusServicePort;
        this.operatorSessionRepository = operatorSessionRepository;
    }

    @Override
    public boolean allowsAccess(final OperatorId operatorId, final Instant issuedAt, final boolean impersonation) {
        if (operatorId == null) {
            return true;
        }
        if (!impersonation && !operatorStatusServicePort.isActive(operatorId)) {
            return false;
        }
        return operatorSessionRepository.findSessionsRevokedAt(operatorId)
                .map(revokedAt -> issuedAt != null && !issuedAt.isBefore(revokedAt.truncatedTo(ChronoUnit.SECONDS)))
                .orElse(true);
    }
}
