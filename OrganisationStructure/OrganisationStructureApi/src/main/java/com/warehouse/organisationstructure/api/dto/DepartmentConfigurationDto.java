package com.warehouse.organisationstructure.api.dto;

public record DepartmentConfigurationDto(boolean autoAssignDeliveryAreas,
                                         boolean autoConnectDepartments,
                                         boolean preventDepartmentDeletion,
                                         boolean preventDepartmentArchiving,
                                         DepartmentDeliveryAreaPolicyDto deliveryAreaPolicy,
                                         DepartmentConnectionPolicyDto connectionPolicy,
                                         DepartmentLifecyclePolicyDto lifecyclePolicy,
                                         DepartmentCapacityPolicyDto capacityPolicy) {

    public DepartmentConfigurationDto(final boolean autoAssignDeliveryAreas,
                                      final boolean autoConnectDepartments,
                                      final boolean preventDepartmentDeletion,
                                      final boolean preventDepartmentArchiving) {
        this(autoAssignDeliveryAreas, autoConnectDepartments, preventDepartmentDeletion, preventDepartmentArchiving,
                null, null, null, null);
    }
}
