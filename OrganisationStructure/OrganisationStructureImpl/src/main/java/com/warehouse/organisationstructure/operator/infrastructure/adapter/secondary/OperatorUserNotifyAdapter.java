package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import com.warehouse.auth.OperatorUserEventPublisher;
import com.warehouse.auth.event.OperatorCreatedEvent;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorUserNotifyPort;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorUserNotifyMapper;

import java.util.concurrent.atomic.AtomicReference;


public class OperatorUserNotifyAdapter implements OperatorUserNotifyPort {

    private final OperatorUserEventPublisher eventPublisher;

    public OperatorUserNotifyAdapter(final OperatorUserEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserId notifyOperatorCreated(final OperatorSnapshot snapshot) {
        final AtomicReference<UserId> userCreatedId = new AtomicReference<>();
        final OperatorCreatedEvent event = OperatorUserNotifyMapper.toEvent(snapshot, userCreatedId::set);
        eventPublisher.sendEvent(event);
        final UserId userId = userCreatedId.get();
        if (userId == null) {
            throw new IllegalStateException("Operator admin user was not created");
        }
        return userId;
    }
}
