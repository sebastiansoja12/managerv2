package com.warehouse.organisationstructure.department.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.application.port.secondary.DepartmentStatusPolicyServicePort;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentStatusPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentLifecyclePolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentWorkTransferMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;

public class DepartmentStatusPolicyServiceAdapter implements DepartmentStatusPolicyServicePort {

    private final OperatorConfigurationPort operatorConfigurationPort;

    public DepartmentStatusPolicyServiceAdapter(final OperatorConfigurationPort operatorConfigurationPort) {
        this.operatorConfigurationPort = operatorConfigurationPort;
    }

    @Override
    public DepartmentStatusPolicy policyFor(final DepartmentId departmentId) {
        final DepartmentConfiguration configuration = operatorConfigurationPort.getDepartmentConfiguration(departmentId);
        final DepartmentLifecyclePolicy lifecyclePolicy = configuration.getLifecyclePolicy();
        return new DepartmentStatusPolicy(configuration.isPreventDepartmentDeletion(),
                configuration.isPreventDepartmentArchiving(),
                lifecyclePolicy.isRequireStatusChangeReason(),
                lifecyclePolicy.isBlockDeactivationWithActiveWork(),
                lifecyclePolicy.getWorkTransferOnDeactivation() == DepartmentWorkTransferMode.AUTOMATIC,
                lifecyclePolicy.getAutoArchiveInactiveAfterDays());
    }
}
