package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class FleetConfiguration {

    private final int vehicleInspectionReminderDays;
    private final int vehicleInsuranceReminderDays;
    private final int maxDriverShiftHours;
    private final int requiredBreakMinutes;
    private final boolean requirePreTripChecklist;
    private final int gpsPositionIntervalSeconds;
    private final int defaultVehiclePalletCapacity;

    public FleetConfiguration(final int vehicleInspectionReminderDays,
                              final int vehicleInsuranceReminderDays,
                              final int maxDriverShiftHours,
                              final int requiredBreakMinutes,
                              final boolean requirePreTripChecklist,
                              final int gpsPositionIntervalSeconds,
                              final int defaultVehiclePalletCapacity) {
        this.vehicleInspectionReminderDays = vehicleInspectionReminderDays;
        this.vehicleInsuranceReminderDays = vehicleInsuranceReminderDays;
        this.maxDriverShiftHours = maxDriverShiftHours;
        this.requiredBreakMinutes = requiredBreakMinutes;
        this.requirePreTripChecklist = requirePreTripChecklist;
        this.gpsPositionIntervalSeconds = gpsPositionIntervalSeconds;
        this.defaultVehiclePalletCapacity = defaultVehiclePalletCapacity;
    }

    public static FleetConfiguration defaultConfiguration() {
        return new FleetConfiguration(
                30,
                30,
                9,
                45,
                true,
                30,
                18
        );
    }

    public int getVehicleInspectionReminderDays() { return vehicleInspectionReminderDays; }
    public int getVehicleInsuranceReminderDays() { return vehicleInsuranceReminderDays; }
    public int getMaxDriverShiftHours() { return maxDriverShiftHours; }
    public int getRequiredBreakMinutes() { return requiredBreakMinutes; }
    public boolean isRequirePreTripChecklist() { return requirePreTripChecklist; }
    public int getGpsPositionIntervalSeconds() { return gpsPositionIntervalSeconds; }
    public int getDefaultVehiclePalletCapacity() { return defaultVehiclePalletCapacity; }
}
