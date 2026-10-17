package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.primary.command.EraseOperatorDataCommand;
import com.warehouse.platformadministration.application.port.secondary.OperatorCourierServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorDataErasureRepository;
import com.warehouse.platformadministration.application.port.secondary.OperatorDepartmentServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorDeviceServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorSessionServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorShipmentSettingsServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorUserServicePort;
import com.warehouse.platformadministration.application.port.secondary.PlatformActorServicePort;
import com.warehouse.platformadministration.application.port.secondary.PlatformAuditRepository;
import com.warehouse.platformadministration.domain.exception.OperatorDataErasureNotAllowedException;
import com.warehouse.platformadministration.domain.exception.PlatformActorRequiredException;
import com.warehouse.platformadministration.domain.model.OperatorDataErasure;
import com.warehouse.platformadministration.domain.model.OperatorDataExport;
import com.warehouse.platformadministration.domain.vo.OperatorProfile;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.PlatformActor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OperatorDataPortImplTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private static final OperatorScope SCOPE = new OperatorScope(OPERATOR_ID, new UserId(1L));

    private final OperatorServicePort operatorServicePort = mock(OperatorServicePort.class);

    private final OperatorUserServicePort operatorUserServicePort = mock(OperatorUserServicePort.class);

    private final OperatorDepartmentServicePort operatorDepartmentServicePort = mock(OperatorDepartmentServicePort.class);

    private final OperatorCourierServicePort operatorCourierServicePort = mock(OperatorCourierServicePort.class);

    private final OperatorDeviceServicePort operatorDeviceServicePort = mock(OperatorDeviceServicePort.class);

    private final OperatorShipmentSettingsServicePort settingsServicePort = mock(OperatorShipmentSettingsServicePort.class);

    private final OperatorSessionServicePort operatorSessionServicePort = mock(OperatorSessionServicePort.class);

    private final PlatformAuditRepository platformAuditRepository = mock(PlatformAuditRepository.class);

    private final OperatorDataErasureRepository erasureRepository = mock(OperatorDataErasureRepository.class);

    private final PlatformActorServicePort platformActorServicePort = mock(PlatformActorServicePort.class);

    private final OperatorDataPort port = new OperatorDataPortImpl(operatorServicePort, operatorUserServicePort,
            operatorDepartmentServicePort, operatorCourierServicePort, operatorDeviceServicePort, settingsServicePort,
            operatorSessionServicePort, platformAuditRepository, erasureRepository, platformActorServicePort,
            Clock.fixed(NOW, ZoneOffset.UTC));

    @BeforeEach
    void setUp() {
        when(operatorServicePort.findScope(OPERATOR_ID)).thenReturn(Optional.of(SCOPE));
        when(platformActorServicePort.currentActor()).thenReturn(Optional.of(
                new PlatformActor(new PlatformOwnerId(1L), "owner", false, null, null)));
        when(erasureRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldExportOperatorDataWithEmptySettingsWhenMissing() {
        when(operatorServicePort.findProfile(OPERATOR_ID)).thenReturn(Optional.of(profile("ACTIVE")));
        when(settingsServicePort.find(SCOPE)).thenReturn(Optional.empty());
        when(operatorUserServicePort.findAll(SCOPE)).thenReturn(List.of());

        final OperatorDataExport export = port.export(OPERATOR_ID);

        assertThat(export.exportedAt()).isEqualTo(NOW);
        assertThat(export.exportedBy()).isEqualTo("owner");
        assertThat(export.shipmentSettings().values()).isEmpty();
        verify(platformAuditRepository).findLatest(OPERATOR_ID, OperatorDataPortImpl.AUDIT_EXPORT_LIMIT);
    }

    @Test
    void shouldRefuseErasureOfActiveOperator() {
        when(operatorServicePort.findProfile(OPERATOR_ID)).thenReturn(Optional.of(profile("ACTIVE")));

        final OperatorDataErasureNotAllowedException exception = assertThrows(
                OperatorDataErasureNotAllowedException.class,
                () -> port.erase(OPERATOR_ID, new EraseOperatorDataCommand("Acme")));

        assertThat(exception.getReason()).isEqualTo(OperatorDataErasure.OPERATOR_ACTIVE);
        verify(operatorUserServicePort, never()).anonymizeAll(any());
    }

    @Test
    void shouldRefuseErasureWithoutMatchingConfirmation() {
        when(operatorServicePort.findProfile(OPERATOR_ID)).thenReturn(Optional.of(profile("INACTIVE")));

        final OperatorDataErasureNotAllowedException exception = assertThrows(
                OperatorDataErasureNotAllowedException.class,
                () -> port.erase(OPERATOR_ID, new EraseOperatorDataCommand("acme")));

        assertThat(exception.getReason()).isEqualTo(OperatorDataErasure.CONFIRMATION_MISMATCH);
    }

    @Test
    void shouldRefuseErasureDuringImpersonation() {
        when(platformActorServicePort.currentActor()).thenReturn(Optional.of(
                new PlatformActor(new PlatformOwnerId(1L), "owner", true, new UserId(5L), OPERATOR_ID)));

        assertThrows(PlatformActorRequiredException.class,
                () -> port.erase(OPERATOR_ID, new EraseOperatorDataCommand("Acme")));
    }

    @Test
    void shouldRevokeSessionsBeforeAnonymizingAndRecordErasure() {
        when(operatorServicePort.findProfile(OPERATOR_ID)).thenReturn(Optional.of(profile("INACTIVE")));
        when(operatorUserServicePort.anonymizeAll(SCOPE)).thenReturn(4);
        when(operatorCourierServicePort.anonymizeAll(SCOPE)).thenReturn(2);
        when(operatorDeviceServicePort.retireAll(SCOPE)).thenReturn(3);

        final OperatorDataErasure erasure = port.erase(OPERATOR_ID, new EraseOperatorDataCommand(" Acme "));

        final InOrder order = inOrder(operatorSessionServicePort, operatorUserServicePort, erasureRepository);
        order.verify(operatorSessionServicePort).revokeSessions(OPERATOR_ID);
        order.verify(operatorUserServicePort).anonymizeAll(SCOPE);
        order.verify(erasureRepository).save(any());
        assertThat(erasure).isEqualTo(new OperatorDataErasure(null, OPERATOR_ID, "owner", NOW, 4, 2, 3));
    }

    private OperatorProfile profile(final String status) {
        return new OperatorProfile(OPERATOR_ID, "Acme", "1234567890", "office@acme.pl", "+48123", null, null, null,
                status, NOW);
    }
}
