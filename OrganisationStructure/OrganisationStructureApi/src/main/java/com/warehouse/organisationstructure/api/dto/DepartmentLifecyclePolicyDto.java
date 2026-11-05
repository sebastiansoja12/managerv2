package com.warehouse.organisationstructure.api.dto;

public record DepartmentLifecyclePolicyDto(
        boolean requireStatusChangeReason,
        boolean blockDeactivationWithActiveWork,
        DepartmentWorkTransferModeDto workTransferOnDeactivation,
        int autoArchiveInactiveAfterDays
) {
}
