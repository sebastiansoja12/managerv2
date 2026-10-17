package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
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
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.exception.PlatformActorRequiredException;
import com.warehouse.platformadministration.domain.model.OperatorDataErasure;
import com.warehouse.platformadministration.domain.model.OperatorDataExport;
import com.warehouse.platformadministration.domain.vo.OperatorProfile;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.OperatorShipmentSettings;
import com.warehouse.platformadministration.domain.vo.PlatformActor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class OperatorDataPortImpl implements OperatorDataPort {

    static final int AUDIT_EXPORT_LIMIT = 500;

    private final OperatorServicePort operatorServicePort;

    private final OperatorUserServicePort operatorUserServicePort;

    private final OperatorDepartmentServicePort operatorDepartmentServicePort;

    private final OperatorCourierServicePort operatorCourierServicePort;

    private final OperatorDeviceServicePort operatorDeviceServicePort;

    private final OperatorShipmentSettingsServicePort operatorShipmentSettingsServicePort;

    private final OperatorSessionServicePort operatorSessionServicePort;

    private final PlatformAuditRepository platformAuditRepository;

    private final OperatorDataErasureRepository operatorDataErasureRepository;

    private final PlatformActorServicePort platformActorServicePort;

    private final Clock clock;

    public OperatorDataPortImpl(final OperatorServicePort operatorServicePort,
                                final OperatorUserServicePort operatorUserServicePort,
                                final OperatorDepartmentServicePort operatorDepartmentServicePort,
                                final OperatorCourierServicePort operatorCourierServicePort,
                                final OperatorDeviceServicePort operatorDeviceServicePort,
                                final OperatorShipmentSettingsServicePort operatorShipmentSettingsServicePort,
                                final OperatorSessionServicePort operatorSessionServicePort,
                                final PlatformAuditRepository platformAuditRepository,
                                final OperatorDataErasureRepository operatorDataErasureRepository,
                                final PlatformActorServicePort platformActorServicePort,
                                final Clock clock) {
        this.operatorServicePort = operatorServicePort;
        this.operatorUserServicePort = operatorUserServicePort;
        this.operatorDepartmentServicePort = operatorDepartmentServicePort;
        this.operatorCourierServicePort = operatorCourierServicePort;
        this.operatorDeviceServicePort = operatorDeviceServicePort;
        this.operatorShipmentSettingsServicePort = operatorShipmentSettingsServicePort;
        this.operatorSessionServicePort = operatorSessionServicePort;
        this.platformAuditRepository = platformAuditRepository;
        this.operatorDataErasureRepository = operatorDataErasureRepository;
        this.platformActorServicePort = platformActorServicePort;
        this.clock = clock;
    }

    @Override
    public OperatorDataExport export(final OperatorId operatorId) {
        final OperatorScope scope = scope(operatorId);
        final OperatorProfile profile = profile(operatorId);
        final PlatformActor actor = requirePanelActor();
        log.info("Platform owner {} exported data of operator {}", actor.username(), operatorId.value());
        return new OperatorDataExport(
                Instant.now(clock),
                actor.username(),
                profile,
                operatorUserServicePort.findAll(scope),
                operatorDepartmentServicePort.findAll(scope),
                operatorCourierServicePort.findAll(scope),
                operatorDeviceServicePort.findAll(scope),
                operatorUserServicePort.findApiKeys(scope),
                operatorShipmentSettingsServicePort.find(scope).orElseGet(() -> new OperatorShipmentSettings(Map.of())),
                platformAuditRepository.findLatest(operatorId, AUDIT_EXPORT_LIMIT));
    }

    @Override
    public OperatorDataErasure erase(final OperatorId operatorId, final EraseOperatorDataCommand command) {
        final OperatorScope scope = scope(operatorId);
        final PlatformActor actor = requirePanelActor();
        OperatorDataErasure.verify(profile(operatorId), command.confirmation());
        log.warn("Platform owner {} started data erasure of operator {}", actor.username(), operatorId.value());
        operatorSessionServicePort.revokeSessions(operatorId);
        final int users = operatorUserServicePort.anonymizeAll(scope);
        final int couriers = operatorCourierServicePort.anonymizeAll(scope);
        final int devices = operatorDeviceServicePort.retireAll(scope);
        final OperatorDataErasure erasure = operatorDataErasureRepository.save(OperatorDataErasure.completed(
                operatorId, actor.username(), Instant.now(clock), users, couriers, devices));
        log.warn("Data of operator {} erased: {} users, {} couriers, {} devices", operatorId.value(), users,
                couriers, devices);
        return erasure;
    }

    @Override
    public Optional<OperatorDataErasure> findLastErasure(final OperatorId operatorId) {
        scope(operatorId);
        return operatorDataErasureRepository.findLatest(operatorId);
    }

    private PlatformActor requirePanelActor() {
        return platformActorServicePort.currentActor()
                .filter(actor -> !actor.impersonation())
                .orElseThrow(PlatformActorRequiredException::new);
    }

    private OperatorProfile profile(final OperatorId operatorId) {
        return operatorServicePort.findProfile(operatorId)
                .orElseThrow(() -> new OperatorNotFoundException(operatorId));
    }

    private OperatorScope scope(final OperatorId operatorId) {
        return operatorServicePort.findScope(operatorId)
                .orElseThrow(() -> new OperatorNotFoundException(operatorId));
    }
}
