package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.api.event.OperatorInitialDepartmentCreateEvent;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorDepartmentNotifyPort;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorDepartmentNotifyMapper;

public class OperatorDepartmentNotifyAdapter implements OperatorDepartmentNotifyPort {

    private final ApplicationEventPublisher eventPublisher;

    public OperatorDepartmentNotifyAdapter(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void notifyOperatorCreated(final OperatorSnapshot snapshot, final UserId adminUserId) {
        final OperatorInitialDepartmentCreateEvent event = OperatorDepartmentNotifyMapper.toEvent(snapshot, adminUserId);
        eventPublisher.publishEvent(event);
    }
}
