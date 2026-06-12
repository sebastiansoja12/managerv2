package com.warehouse.organisationstructure.operator.domain.listener;

import java.time.Instant;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.operator.domain.event.OperatorCreatedEvent;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorConfigurationEventServicePort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorContextServicePort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorDepartmentNotifyPort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorGeocodingConfigurationEventServicePort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorUserNotifyPort;
import com.warehouse.organisationstructure.operator.domain.service.OperatorService;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

@Component
public class OperatorDomainEventListener {

    private final OperatorDepartmentNotifyPort operatorDepartmentNotifyPort;
    private final OperatorGeocodingConfigurationEventServicePort operatorGeocodingConfigurationEventServicePort;
    private final OperatorConfigurationEventServicePort operatorConfigurationEventServicePort;
    private final OperatorUserNotifyPort operatorUserNotifyPort;
    private final OperatorService operatorService;
    private final OperatorContextServicePort operatorContextServicePort;

    public OperatorDomainEventListener(
            final OperatorDepartmentNotifyPort operatorDepartmentNotifyPort,
            final OperatorGeocodingConfigurationEventServicePort operatorGeocodingConfigurationEventServicePort,
            final OperatorConfigurationEventServicePort operatorConfigurationEventServicePort,
            final OperatorUserNotifyPort operatorUserNotifyPort,
            final OperatorService operatorService,
            final OperatorContextServicePort operatorContextServicePort) {
        this.operatorDepartmentNotifyPort = operatorDepartmentNotifyPort;
        this.operatorGeocodingConfigurationEventServicePort = operatorGeocodingConfigurationEventServicePort;
        this.operatorConfigurationEventServicePort = operatorConfigurationEventServicePort;
        this.operatorUserNotifyPort = operatorUserNotifyPort;
        this.operatorService = operatorService;
        this.operatorContextServicePort = operatorContextServicePort;
    }

    @EventListener
    public void handle(final OperatorCreatedEvent event) {
        final OperatorSnapshot snapshot = event.getSnapshot();
        operatorContextServicePort.runInContext(
                snapshot.operatorId(),
                () -> provisionOperator(snapshot, event.getTimestamp())
        );
    }

    private void provisionOperator(final OperatorSnapshot snapshot, final Instant timestamp) {
        final UserId userId = operatorUserNotifyPort.notifyOperatorCreated(
                snapshot,
                reservedUserId -> operatorContextServicePort.runInContext(
                        snapshot.operatorId(),
                        reservedUserId,
                        () -> publishResourcesRequiredByUser(snapshot, timestamp)
                )
        );
        operatorService.assignRegisteringUser(snapshot.operatorId(), userId);
        operatorContextServicePort.runInContext(
                snapshot.operatorId(),
                userId,
                () -> operatorConfigurationEventServicePort.publishOperatorCreated(snapshot, timestamp)
        );
    }

    private void publishResourcesRequiredByUser(final OperatorSnapshot snapshot, final Instant timestamp) {
        operatorGeocodingConfigurationEventServicePort.publishOperatorCreated(snapshot, timestamp);
        operatorDepartmentNotifyPort.notifyOperatorCreated(snapshot);
    }
}
