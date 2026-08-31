package com.warehouse.organisationstructure.department.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.primary.event.AdminUserCommand;
import com.warehouse.commonassets.enumeration.Language;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.organisationstructure.department.domain.event.DepartmentAdminChanged;
import com.warehouse.organisationstructure.department.domain.port.secondary.TenantAdminProvisioningPort;
import com.warehouse.organisationstructure.department.domain.registry.DomainRegistry;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;

public class TenantAdminProvisioningAdapter implements TenantAdminProvisioningPort {

    @Override
    public void createInitialAdminUser(final DepartmentSnapshot snapshot) {
        final DepartmentCode departmentCode = new DepartmentCode(snapshot.departmentCode().getValue());
        final String telephoneNumber = snapshot.telephoneNumber();
        final String email = snapshot.email();
		InfrastructureRegistry.eventPublisher().publishEvent(new AdminUserCommand(departmentCode, telephoneNumber,
				email, Language.EN,
                userId -> DomainRegistry.eventPublisher().publishEvent(
						new DepartmentAdminChanged(snapshot.departmentCode(), userId))));
    }
}
