package com.warehouse.department.application.listener;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.department.api.dto.DepartmentCodeDto;
import com.warehouse.department.api.event.DepartmentUserChangedIntegrationEvent;
import com.warehouse.department.domain.event.DepartmentEmailChanged;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class DepartmentUserIntegrationEventListener {

    private final IntegrationEventPublisher integrationEventPublisher;

    public DepartmentUserIntegrationEventListener(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @EventListener
    public void handle(final DepartmentEmailChanged event) {
        final DepartmentSnapshot snapshot = event.getSnapshot();
        this.integrationEventPublisher.publish(new DepartmentUserChangedIntegrationEvent(
                new DepartmentCodeDto(snapshot.departmentCode().getValue()),
                snapshot.adminUserId(),
                snapshot.telephoneNumber(),
                snapshot.email(),
                event.getTimestamp()));
    }
}
