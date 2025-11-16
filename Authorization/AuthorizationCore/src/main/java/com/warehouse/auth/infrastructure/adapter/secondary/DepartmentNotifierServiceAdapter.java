package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.secondary.DepartmentNotifierServicePort;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.context.ApplicationEventPublisher;

public class DepartmentNotifierServiceAdapter implements DepartmentNotifierServicePort {

    private final ApplicationEventPublisher eventPublisher;

    public DepartmentNotifierServiceAdapter(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void notifyDepartmentWithAdminUser(final DepartmentCode departmentCode, final UserId userId) {
        //this.eventPublisher.publishEvent(new DepartmentAdminEvent(departmentCode, userId));
    }
}
