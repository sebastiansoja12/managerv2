package com.warehouse.department.application.listener;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.department.api.dto.DepartmentCodeDto;
import com.warehouse.department.api.dto.DepartmentIdDto;
import com.warehouse.department.api.dto.DepartmentStatusDto;
import com.warehouse.department.api.event.DepartmentStatusChangedIntegrationEvent;
import com.warehouse.department.domain.event.DepartmentArchived;
import com.warehouse.department.domain.event.DepartmentChanged;
import com.warehouse.department.domain.event.DepartmentDeleted;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class DepartmentStatusIntegrationEventListener {

    private final IntegrationEventPublisher integrationEventPublisher;

    public DepartmentStatusIntegrationEventListener(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @EventListener
    public void handle(final DepartmentArchived event) {
        publish(event, DepartmentStatusDto.ARCHIVED);
    }

    @EventListener
    public void handle(final DepartmentDeleted event) {
        publish(event, DepartmentStatusDto.DELETED);
    }

    private void publish(final DepartmentChanged event, final DepartmentStatusDto status) {
        this.integrationEventPublisher.publish(new DepartmentStatusChangedIntegrationEvent(
                new DepartmentIdDto(event.getSnapshot().departmentId().getValue()),
                new DepartmentCodeDto(event.getSnapshot().departmentCode().getValue()),
                status));
    }
}
