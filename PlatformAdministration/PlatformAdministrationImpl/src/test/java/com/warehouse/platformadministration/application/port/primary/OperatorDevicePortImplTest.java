package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.secondary.OperatorDeviceServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorUserServicePort;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OperatorDevicePortImplTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private static final OperatorScope SCOPE = new OperatorScope(OPERATOR_ID, new UserId(1L));

    private final OperatorServicePort operatorServicePort = mock(OperatorServicePort.class);

    private final OperatorDeviceServicePort operatorDeviceServicePort = mock(OperatorDeviceServicePort.class);

    private final OperatorUserServicePort operatorUserServicePort = mock(OperatorUserServicePort.class);

    private final OperatorDevicePort port = new OperatorDevicePortImpl(operatorServicePort, operatorDeviceServicePort,
            operatorUserServicePort);

    @BeforeEach
    void setUp() {
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.of(SCOPE));
    }

    @Test
    void shouldRevokePairedDevice() {
        when(operatorDeviceServicePort.revoke(SCOPE, "T-1")).thenReturn(true);

        port.revokeDevice(OPERATOR_ID, "T-1");

        verify(operatorDeviceServicePort).revoke(SCOPE, "T-1");
    }

    @Test
    void shouldReportDeviceWithoutPairing() {
        when(operatorDeviceServicePort.revoke(SCOPE, "T-1")).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> port.revokeDevice(OPERATOR_ID, "T-1"));
    }

    @Test
    void shouldAllowOnlyActiveOrBlockedStatus() {
        assertThrows(IllegalArgumentException.class, () -> port.changeDeviceStatus(OPERATOR_ID, "T-1", "LOST"));

        verify(operatorDeviceServicePort, never()).changeStatus(any(), anyString(), anyString());
    }

    @Test
    void shouldBlockDevice() {
        port.changeDeviceStatus(OPERATOR_ID, "T-1", "BLOCKED");

        verify(operatorDeviceServicePort).changeStatus(SCOPE, "T-1", "BLOCKED");
    }

    @Test
    void shouldRevokeUserApiKey() {
        port.revokeApiKey(OPERATOR_ID, new UserId(5L));

        verify(operatorUserServicePort).revokeApiKey(SCOPE, new UserId(5L));
    }
}
