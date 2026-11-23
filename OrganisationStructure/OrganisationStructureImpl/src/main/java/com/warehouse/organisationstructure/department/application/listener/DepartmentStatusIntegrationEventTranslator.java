package com.warehouse.organisationstructure.department.application.listener;

import com.warehouse.organisationstructure.department.api.dto.DepartmentCodeDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentIdDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentStatusDto;
import com.warehouse.organisationstructure.department.api.event.DepartmentStatusChangedIntegrationEvent;
import com.warehouse.organisationstructure.department.domain.event.DepartmentArchived;
import com.warehouse.organisationstructure.department.domain.event.DepartmentChanged;
import com.warehouse.organisationstructure.department.domain.event.DepartmentDeleted;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import org.springframework.stereotype.Component;

@Component
public class DepartmentStatusIntegrationEventTranslator {

    @IntegrationEventTranslation
    public DepartmentStatusChangedIntegrationEvent translate(final DepartmentArchived event) {
        return statusChanged(event, DepartmentStatusDto.ARCHIVED);
    }

    @IntegrationEventTranslation
    public DepartmentStatusChangedIntegrationEvent translate(final DepartmentDeleted event) {
        return statusChanged(event, DepartmentStatusDto.DELETED);
    }

    private DepartmentStatusChangedIntegrationEvent statusChanged(final DepartmentChanged event,
                                                                  final DepartmentStatusDto status) {
        return new DepartmentStatusChangedIntegrationEvent(
                new DepartmentIdDto(event.getSnapshot().departmentId().getValue()),
                new DepartmentCodeDto(event.getSnapshot().departmentCode().getValue()),
                status,
                event.getSnapshot().statusChangeReason());
    }
}
