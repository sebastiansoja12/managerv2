package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.primary.command.ChangeMaintenanceModeCommand;
import com.warehouse.platformadministration.application.port.secondary.MaintenanceModeRepository;
import com.warehouse.platformadministration.application.port.secondary.PlatformActorServicePort;
import com.warehouse.platformadministration.domain.exception.PlatformActorRequiredException;
import com.warehouse.platformadministration.domain.model.MaintenanceMode;
import com.warehouse.platformadministration.domain.vo.PlatformActor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class MaintenanceModePortImplTest {

    private static final Instant NOW = Instant.parse("2026-10-17T10:00:00Z");

    private final MaintenanceModeRepository maintenanceModeRepository = mock(MaintenanceModeRepository.class);

    private final PlatformActorServicePort platformActorServicePort = mock(PlatformActorServicePort.class);

    private final MaintenanceModePort port = new MaintenanceModePortImpl(maintenanceModeRepository,
            platformActorServicePort, Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void shouldEnableMaintenanceOnBehalfOfPlatformOwner() {
        when(platformActorServicePort.currentActor()).thenReturn(Optional.of(
                new PlatformActor(new PlatformOwnerId(1L), "owner", false, null, null)));
        when(maintenanceModeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        final MaintenanceMode maintenanceMode = port.change(new ChangeMaintenanceModeCommand(true, "Aktualizacja"));

        final ArgumentCaptor<MaintenanceMode> saved = ArgumentCaptor.forClass(MaintenanceMode.class);
        verify(maintenanceModeRepository).save(saved.capture());
        assertThat(saved.getValue()).isEqualTo(new MaintenanceMode(true, "Aktualizacja", "owner", NOW));
        assertThat(maintenanceMode.enabled()).isTrue();
    }

    @Test
    void shouldNotAllowChangingMaintenanceDuringImpersonation() {
        when(platformActorServicePort.currentActor()).thenReturn(Optional.of(
                new PlatformActor(new PlatformOwnerId(1L), "owner", true, new UserId(5L), OperatorId.of(20L))));

        assertThrows(PlatformActorRequiredException.class,
                () -> port.change(new ChangeMaintenanceModeCommand(true, null)));

        verify(maintenanceModeRepository, never()).save(any());
    }

    @Test
    void shouldNotResolveActorWhenMaintenanceIsDisabled() {
        when(maintenanceModeRepository.current()).thenReturn(MaintenanceMode.disabled());

        assertThat(port.isBlocked("/shipments", "GET")).isFalse();

        verifyNoInteractions(platformActorServicePort);
    }

    @Test
    void shouldBlockRegularUserDuringMaintenance() {
        when(maintenanceModeRepository.current()).thenReturn(new MaintenanceMode(true, null, "owner", NOW));
        when(platformActorServicePort.currentActor()).thenReturn(Optional.empty());

        assertThat(port.isBlocked("/shipments", "GET")).isTrue();
    }

    @Test
    void shouldLetImpersonatingOwnerWorkDuringMaintenance() {
        when(maintenanceModeRepository.current()).thenReturn(new MaintenanceMode(true, null, "owner", NOW));
        when(platformActorServicePort.currentActor()).thenReturn(Optional.of(
                new PlatformActor(new PlatformOwnerId(1L), "owner", true, new UserId(5L), OperatorId.of(20L))));

        assertThat(port.isBlocked("/shipments", "GET")).isFalse();
    }
}
