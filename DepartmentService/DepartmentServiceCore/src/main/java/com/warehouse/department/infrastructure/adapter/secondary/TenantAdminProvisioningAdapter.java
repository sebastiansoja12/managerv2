package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.auth.infrastructure.adapter.primary.event.AdminUserCommand;
import com.warehouse.department.domain.port.secondary.TenantAdminProvisioningPort;
import com.warehouse.department.domain.vo.DepartmentSnapshot;

public class TenantAdminProvisioningAdapter implements TenantAdminProvisioningPort {

    private final ApplicationEventPublisher applicationEventPublisher;

    public TenantAdminProvisioningAdapter(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void createInitialAdminUser(final DepartmentSnapshot snapshot) {
        final DepartmentCode departmentCode = new DepartmentCode(snapshot.departmentCode().getValue());
        final String telephoneNumber = snapshot.telephoneNumber();
        final String email = snapshot.email();
        this.applicationEventPublisher.publishEvent(new AdminUserCommand(departmentCode, telephoneNumber, email));
    }
}
