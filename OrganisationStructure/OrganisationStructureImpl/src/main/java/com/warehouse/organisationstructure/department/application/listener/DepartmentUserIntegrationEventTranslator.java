package com.warehouse.organisationstructure.department.application.listener;

import com.warehouse.organisationstructure.department.api.dto.DepartmentCodeDto;
import com.warehouse.organisationstructure.department.api.event.DepartmentUserChangedIntegrationEvent;
import com.warehouse.organisationstructure.department.domain.event.DepartmentEmailChanged;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import org.springframework.stereotype.Component;

@Component
public class DepartmentUserIntegrationEventTranslator {

    @IntegrationEventTranslation
    public DepartmentUserChangedIntegrationEvent translate(final DepartmentEmailChanged event) {
        final DepartmentSnapshot snapshot = event.getSnapshot();
        return new DepartmentUserChangedIntegrationEvent(
                new DepartmentCodeDto(snapshot.departmentCode().getValue()),
                snapshot.adminUserId(),
                snapshot.telephoneNumber(),
                snapshot.email(),
                event.getTimestamp());
    }
}
