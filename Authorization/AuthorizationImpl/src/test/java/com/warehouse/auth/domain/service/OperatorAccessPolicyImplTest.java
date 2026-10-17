package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.port.secondary.OperatorSessionRepository;
import com.warehouse.auth.domain.port.secondary.OperatorStatusServicePort;
import com.warehouse.commonassets.identificator.OperatorId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class OperatorAccessPolicyImplTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private static final Instant REVOKED_AT = Instant.parse("2026-10-17T10:00:00.700Z");

    private final OperatorStatusServicePort operatorStatusServicePort = mock(OperatorStatusServicePort.class);

    private final OperatorSessionRepository operatorSessionRepository = mock(OperatorSessionRepository.class);

    private final OperatorAccessPolicy policy = new OperatorAccessPolicyImpl(operatorStatusServicePort,
            operatorSessionRepository);

    @Test
    void shouldAllowActiveOperatorWithoutRevokedSessions() {
        when(operatorStatusServicePort.isActive(OPERATOR_ID)).thenReturn(true);
        when(operatorSessionRepository.findSessionsRevokedAt(OPERATOR_ID)).thenReturn(Optional.empty());

        assertThat(policy.allowsAccess(OPERATOR_ID, Instant.now(), false)).isTrue();
    }

    @Test
    void shouldRejectUserOfBlockedOperator() {
        when(operatorStatusServicePort.isActive(OPERATOR_ID)).thenReturn(false);

        assertThat(policy.allowsAccess(OPERATOR_ID, Instant.now(), false)).isFalse();
    }

    @Test
    void shouldAllowImpersonationOfBlockedOperator() {
        when(operatorStatusServicePort.isActive(OPERATOR_ID)).thenReturn(false);
        when(operatorSessionRepository.findSessionsRevokedAt(OPERATOR_ID)).thenReturn(Optional.empty());

        assertThat(policy.allowsAccess(OPERATOR_ID, Instant.now(), true)).isTrue();
    }

    @Test
    void shouldRejectTokenIssuedBeforeSessionsWereRevoked() {
        when(operatorStatusServicePort.isActive(OPERATOR_ID)).thenReturn(true);
        when(operatorSessionRepository.findSessionsRevokedAt(OPERATOR_ID)).thenReturn(Optional.of(REVOKED_AT));

        assertThat(policy.allowsAccess(OPERATOR_ID, Instant.parse("2026-10-17T09:59:59Z"), false)).isFalse();
    }

    @Test
    void shouldAllowTokenIssuedInSameSecondAfterRevocation() {
        when(operatorStatusServicePort.isActive(OPERATOR_ID)).thenReturn(true);
        when(operatorSessionRepository.findSessionsRevokedAt(OPERATOR_ID)).thenReturn(Optional.of(REVOKED_AT));

        assertThat(policy.allowsAccess(OPERATOR_ID, Instant.parse("2026-10-17T10:00:00Z"), false)).isTrue();
    }

    @Test
    void shouldRejectTokenWithoutIssueDateAfterRevocation() {
        when(operatorStatusServicePort.isActive(OPERATOR_ID)).thenReturn(true);
        when(operatorSessionRepository.findSessionsRevokedAt(OPERATOR_ID)).thenReturn(Optional.of(REVOKED_AT));

        assertThat(policy.allowsAccess(OPERATOR_ID, null, false)).isFalse();
    }

    @Test
    void shouldAllowAccountsWithoutOperator() {
        assertThat(policy.allowsAccess(null, Instant.now(), false)).isTrue();

        verifyNoInteractions(operatorStatusServicePort, operatorSessionRepository);
    }
}
