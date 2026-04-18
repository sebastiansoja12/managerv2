package com.warehouse.organisationstructure.operator.domain.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.organisationstructure.operator.domain.event.OperatorCreatedEvent;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorDepartmentNotifyPort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorUserNotifyPort;
import com.warehouse.organisationstructure.operator.domain.service.OperatorService;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.commonassets.identificator.UserId;

@Component
public class OperatorDomainEventListener {

    private final OperatorDepartmentNotifyPort operatorDepartmentNotifyPort;
    private final OperatorUserNotifyPort operatorUserNotifyPort;
    private final OperatorService operatorService;

    public OperatorDomainEventListener(final OperatorDepartmentNotifyPort operatorDepartmentNotifyPort,
                                       final OperatorUserNotifyPort operatorUserNotifyPort,
                                       final OperatorService operatorService) {
        this.operatorDepartmentNotifyPort = operatorDepartmentNotifyPort;
        this.operatorUserNotifyPort = operatorUserNotifyPort;
        this.operatorService = operatorService;
    }

    @EventListener
    public void handle(final OperatorCreatedEvent event) {
        final OperatorSnapshot snapshot = event.getSnapshot();
        operatorDepartmentNotifyPort.notifyOperatorCreated(snapshot);
        final UserId userId = operatorUserNotifyPort.notifyOperatorCreated(snapshot);
        operatorService.assignRegisteringUser(snapshot.operatorId(), userId);
    }
}
