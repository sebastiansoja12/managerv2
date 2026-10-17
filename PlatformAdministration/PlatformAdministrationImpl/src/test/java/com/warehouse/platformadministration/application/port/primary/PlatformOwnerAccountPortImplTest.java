package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.secondary.PlatformActorServicePort;
import com.warehouse.platformadministration.application.port.secondary.PlatformOwnerAccountServicePort;
import com.warehouse.platformadministration.domain.exception.PlatformActorRequiredException;
import com.warehouse.platformadministration.domain.vo.PlatformActor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlatformOwnerAccountPortImplTest {

    private final PlatformOwnerAccountServicePort platformOwnerAccountServicePort = mock(PlatformOwnerAccountServicePort.class);

    private final PlatformActorServicePort platformActorServicePort = mock(PlatformActorServicePort.class);

    private final PlatformOwnerAccountPort port = new PlatformOwnerAccountPortImpl(platformOwnerAccountServicePort,
            platformActorServicePort);

    @Test
    void shouldBlockOwnerOnBehalfOfCurrentPlatformOwner() {
        when(platformActorServicePort.currentActor()).thenReturn(Optional.of(
                new PlatformActor(new PlatformOwnerId(1L), "owner", false, null, null)));

        port.block(new PlatformOwnerId(2L));

        verify(platformOwnerAccountServicePort).block(new PlatformOwnerId(2L), new PlatformOwnerId(1L));
    }

    @Test
    void shouldNotAllowBlockingDuringImpersonation() {
        when(platformActorServicePort.currentActor()).thenReturn(Optional.of(
                new PlatformActor(new PlatformOwnerId(1L), "owner", true, new UserId(5L), OperatorId.of(20L))));

        assertThrows(PlatformActorRequiredException.class, () -> port.block(new PlatformOwnerId(2L)));

        verify(platformOwnerAccountServicePort, never()).block(any(), any());
    }

    @Test
    void shouldRequireAuthenticatedPlatformOwnerToBlock() {
        when(platformActorServicePort.currentActor()).thenReturn(Optional.empty());

        assertThrows(PlatformActorRequiredException.class, () -> port.block(new PlatformOwnerId(2L)));
    }
}
