package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.command.RecordAuditedRequestCommand;
import com.warehouse.platformadministration.application.port.secondary.PlatformActorServicePort;
import com.warehouse.platformadministration.application.port.secondary.PlatformAuditRepository;
import com.warehouse.platformadministration.domain.model.PlatformAuditEntry;

import java.util.List;

public class PlatformAuditPortImpl implements PlatformAuditPort {

    private static final int MAX_LIMIT = 500;

    private final PlatformActorServicePort platformActorServicePort;

    private final PlatformAuditRepository platformAuditRepository;

    public PlatformAuditPortImpl(final PlatformActorServicePort platformActorServicePort,
                                 final PlatformAuditRepository platformAuditRepository) {
        this.platformActorServicePort = platformActorServicePort;
        this.platformAuditRepository = platformAuditRepository;
    }

    @Override
    public void record(final RecordAuditedRequestCommand command) {
        platformActorServicePort.currentActor()
                .map(actor -> PlatformAuditEntry.record(actor, command.httpMethod(), command.path(),
                        command.statusCode(), command.failed()))
                .ifPresent(platformAuditRepository::save);
    }

    @Override
    public List<PlatformAuditEntry> findLatest(final OperatorId operatorId, final int limit) {
        return platformAuditRepository.findLatest(operatorId, Math.clamp(limit, 1, MAX_LIMIT));
    }
}
