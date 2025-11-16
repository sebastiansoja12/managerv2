package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.primary.event.AdminUserCommand;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.domain.event.DepartmentAdminChanged;
import com.warehouse.department.domain.port.secondary.TenantAdminProvisioningPort;
import com.warehouse.department.domain.registry.DomainRegistry;
import com.warehouse.department.domain.vo.DepartmentSnapshot;

public class TenantAdminProvisioningAdapter implements TenantAdminProvisioningPort {

    @Override
    public void createInitialAdminUser(final DepartmentSnapshot snapshot) {
        final DepartmentCode departmentCode = new DepartmentCode(snapshot.departmentCode().getValue());
        final String telephoneNumber = snapshot.telephoneNumber();
        final String email = snapshot.email();
        InfrastructureRegistry.eventPublisher().publishEvent(new AdminUserCommand(departmentCode, telephoneNumber, email,
                userId -> DomainRegistry.eventPublisher().publishEvent(
						new DepartmentAdminChanged(snapshot.departmentCode(), userId))));
    }
}
