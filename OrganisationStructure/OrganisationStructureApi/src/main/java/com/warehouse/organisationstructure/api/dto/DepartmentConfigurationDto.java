package com.warehouse.organisationstructure.api.dto;

public record DepartmentConfigurationDto(boolean autoAssignDeliveryAreas,
                                         boolean autoConnectDepartments,
                                         boolean preventDepartmentDeletion,
                                         boolean preventDepartmentArchiving) {
}
