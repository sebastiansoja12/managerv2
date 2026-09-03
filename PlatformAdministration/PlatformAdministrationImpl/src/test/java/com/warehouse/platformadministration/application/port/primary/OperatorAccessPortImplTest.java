package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorSessionServicePort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.OperatorAccess;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OperatorAccessPortImplTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private final OperatorServicePort operatorServicePort = mock(OperatorServicePort.class);

    private final OperatorSessionServicePort operatorSessionServicePort = mock(OperatorSessionServicePort.class);

    private final OperatorAccessPort port = new OperatorAccessPortImpl(operatorServicePort, operatorSessionServicePort);

    @Test
    void shouldReturnActiveSessionsOfOperator() {
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.of(new OperatorScope(OPERATOR_ID, new UserId(1L))));
        when(operatorSessionServicePort.countActiveSessions(OPERATOR_ID)).thenReturn(7L);

        assertThat(port.findAccess(OPERATOR_ID)).isEqualTo(new OperatorAccess(OPERATOR_ID, 7L));
    }

    @Test
    void shouldRevokeSessionsOfExistingOperator() {
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.of(new OperatorScope(OPERATOR_ID, new UserId(1L))));

        port.revokeSessions(OPERATOR_ID);

        verify(operatorSessionServicePort).revokeSessions(OPERATOR_ID);
    }

    @Test
    void shouldNotRevokeSessionsOfUnknownOperator() {
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.empty());

        assertThrows(OperatorNotFoundException.class, () -> port.revokeSessions(OPERATOR_ID));

        verify(operatorSessionServicePort, never()).revokeSessions(any());
    }
}
