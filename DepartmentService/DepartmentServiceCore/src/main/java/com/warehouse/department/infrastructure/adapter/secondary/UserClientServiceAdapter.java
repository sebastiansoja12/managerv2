package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.primary.event.DepartmentUserDeleted;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.domain.port.secondary.UserClientServicePort;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserClientServiceAdapter implements UserClientServicePort {

    @Override
    public void notifyUserDepartmentDeleted(final DepartmentSnapshot snapshot) {
        log.info("Notifying user department deleted event for department {}", snapshot.departmentCode());
        InfrastructureRegistry.eventPublisher()
                .publishEvent(new DepartmentUserDeleted(new DepartmentCode(snapshot.departmentCode().getValue())));
    }
}
