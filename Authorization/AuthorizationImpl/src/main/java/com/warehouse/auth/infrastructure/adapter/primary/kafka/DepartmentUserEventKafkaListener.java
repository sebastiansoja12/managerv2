package com.warehouse.auth.infrastructure.adapter.primary.kafka;

import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.domain.vo.UserDepartmentUpdateRequest;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.organisationstructure.department.api.dto.DepartmentStatusDto;
import com.warehouse.organisationstructure.department.api.event.DepartmentStatusChangedIntegrationEvent;
import com.warehouse.organisationstructure.department.api.event.DepartmentUserChangedIntegrationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;

@Slf4j
@Component
public class DepartmentUserEventKafkaListener {

    private final UserPort userPort;

    public DepartmentUserEventKafkaListener(final UserPort userPort) {
        this.userPort = userPort;
    }

    @IntegrationEventListener(group = "manager-auth-department-users")
    public void handle(final DepartmentUserChangedIntegrationEvent event) {
        final DepartmentCode departmentCode = new DepartmentCode(event.departmentCode().value());
        final UserDepartmentUpdateRequest request = new UserDepartmentUpdateRequest(
                departmentCode,
                event.userId(),
                event.telephoneNumber(),
                event.email()
        );
        this.userPort.changeAdminDepartmentInfo(request);
        log.info("Department user updated for department {}", departmentCode.getValue());
    }

    @IntegrationEventListener(group = "manager-auth-department-users")
    public void handle(final DepartmentStatusChangedIntegrationEvent event) {
        if (event.status() == DepartmentStatusDto.DELETED) {
            final DepartmentCode departmentCode = new DepartmentCode(event.departmentCode().value());
            this.userPort.deleteDataForDepartment(departmentCode);
            log.info("Department user deleted for department {}", departmentCode.getValue());
        }
    }
}
