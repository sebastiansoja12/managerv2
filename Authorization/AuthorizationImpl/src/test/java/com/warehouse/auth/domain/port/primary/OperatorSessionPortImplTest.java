package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.port.secondary.OperatorSessionRepository;
import com.warehouse.commonassets.identificator.OperatorId;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OperatorSessionPortImplTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private final OperatorSessionRepository operatorSessionRepository = mock(OperatorSessionRepository.class);

    private final OperatorSessionPort port = new OperatorSessionPortImpl(operatorSessionRepository,
            Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void shouldCountActiveSessionsAtCurrentTime() {
        when(operatorSessionRepository.countActiveRefreshTokens(OPERATOR_ID, NOW)).thenReturn(4L);

        assertThat(port.countActiveSessions(OPERATOR_ID)).isEqualTo(4L);
    }

    @Test
    void shouldMarkRevocationBeforeRemovingRefreshTokens() {
        port.revokeSessions(OPERATOR_ID);

        final InOrder order = inOrder(operatorSessionRepository);
        order.verify(operatorSessionRepository).markSessionsRevoked(OPERATOR_ID, NOW);
        order.verify(operatorSessionRepository).deleteRefreshTokens(OPERATOR_ID);
    }
}
