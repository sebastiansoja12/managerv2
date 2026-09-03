package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.platformadministration.application.port.primary.command.RecordAuditedRequestCommand;
import com.warehouse.platformadministration.application.port.secondary.PlatformActorServicePort;
import com.warehouse.platformadministration.application.port.secondary.PlatformAuditRepository;
import com.warehouse.platformadministration.domain.model.PlatformAuditEntry;
import com.warehouse.platformadministration.domain.vo.PlatformActor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlatformAuditPortImplTest {

    private final PlatformActorServicePort platformActorServicePort = mock(PlatformActorServicePort.class);

    private final PlatformAuditRepository platformAuditRepository = mock(PlatformAuditRepository.class);

    private final PlatformAuditPort platformAuditPort = new PlatformAuditPortImpl(platformActorServicePort,
            platformAuditRepository);

    @Test
    void shouldSaveEntryWhenRequestWasMadeByPlatformOwner() {
        when(platformActorServicePort.currentActor()).thenReturn(Optional.of(
                new PlatformActor(new PlatformOwnerId(1L), "owner", false, null, null)));

        platformAuditPort.record(new RecordAuditedRequestCommand("POST", "/operators/1/departments", 201, false));

        verify(platformAuditRepository).save(any(PlatformAuditEntry.class));
    }

    @Test
    void shouldIgnoreRequestsOfRegularUsers() {
        when(platformActorServicePort.currentActor()).thenReturn(Optional.empty());

        platformAuditPort.record(new RecordAuditedRequestCommand("POST", "/users", 200, false));

        verify(platformAuditRepository, never()).save(any());
    }
}
