package com.warehouse.organisationstructure.api.dto;

public record ShipmentSlaConfigurationDto(
        int economySlaHours,
        int standardSlaHours,
        int expressSlaHours,
        int flagDelayedAfterHours,
        boolean requireExceptionNote,
        boolean escalateDelaysToDepartmentManager
) {
}
