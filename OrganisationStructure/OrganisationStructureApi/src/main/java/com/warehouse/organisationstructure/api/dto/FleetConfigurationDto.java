package com.warehouse.organisationstructure.api.dto;

public record FleetConfigurationDto(
        int vehicleInspectionReminderDays,
        int vehicleInsuranceReminderDays,
        int maxDriverShiftHours,
        int requiredBreakMinutes,
        boolean requirePreTripChecklist,
        int gpsPositionIntervalSeconds,
        int defaultVehiclePalletCapacity
) {
}
