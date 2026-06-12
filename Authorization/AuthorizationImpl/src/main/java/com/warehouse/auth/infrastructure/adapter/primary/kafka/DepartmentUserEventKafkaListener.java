package com.warehouse.auth.infrastructure.adapter.primary.kafka;

import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.domain.vo.UserDepartmentUpdateRequest;
import com.warehouse.auth.infrastructure.adapter.primary.event.DepartmentUserChanged;
import com.warehouse.auth.infrastructure.adapter.primary.event.DepartmentUserDeleted;
import com.warehouse.commonassets.identificator.DepartmentCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(
        topics = "${manager.kafka.topics.department-user-events:department.user.events}",
        groupId = "${manager.kafka.consumer-groups.department-user-events:${spring.application.name}-auth-department-users}"
)
public class DepartmentUserEventKafkaListener {

    private final UserPort userPort;

    public DepartmentUserEventKafkaListener(final UserPort userPort) {
        this.userPort = userPort;
    }

    @KafkaHandler
    public void handle(final DepartmentUserDeleted event) {
        final DepartmentCode departmentCode = event.departmentCode();
        this.userPort.deleteDataForDepartment(departmentCode);
        log.info("Department user deleted for department {}", departmentCode.getValue());
    }

    @KafkaHandler
    public void handle(final DepartmentUserChanged event) {
        final DepartmentCode departmentCode = event.departmentCode();
        final UserDepartmentUpdateRequest request = new UserDepartmentUpdateRequest(
                departmentCode,
                event.userId(),
                event.telephoneNumber(),
                event.email()
        );
        this.userPort.changeAdminDepartmentInfo(request);
        log.info("Department user updated for department {}", departmentCode.getValue());
    }
}
