package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.primary.event.DepartmentUserChanged;
import com.warehouse.auth.infrastructure.adapter.primary.event.DepartmentUserDeleted;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.domain.port.secondary.UserClientServicePort;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public class UserClientServiceAdapter implements UserClientServicePort {
    
    private final ApplicationEventPublisher eventPublisher;

    public UserClientServiceAdapter(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void notifyUserDepartmentDeleted(final DepartmentSnapshot snapshot) {
        log.info("Notifying user department deleted event for department {}", snapshot.departmentCode());
        eventPublisher.publishEvent(new DepartmentUserDeleted(new DepartmentCode(snapshot.departmentCode().getValue())));
    }

    @Override
    public void notifyUserDepartmentChanged(final DepartmentSnapshot snapshot) {
        log.info("Notifying user data changed for department: {}", snapshot.departmentCode());
		eventPublisher.publishEvent(new DepartmentUserChanged(new DepartmentCode(snapshot.departmentCode().getValue()),
				snapshot.adminUserId(), snapshot.telephoneNumber(), snapshot.email()));
    }
}
