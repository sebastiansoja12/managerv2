package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.FleetConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FleetConfigurationEmbeddable {

    @Column(name = "fleet_vehicle_inspection_reminder_days")
    private int vehicleInspectionReminderDays;

    @Column(name = "fleet_vehicle_insurance_reminder_days")
    private int vehicleInsuranceReminderDays;

    @Column(name = "fleet_max_driver_shift_hours")
    private int maxDriverShiftHours;

    @Column(name = "fleet_required_break_minutes")
    private int requiredBreakMinutes;

    @Column(name = "fleet_require_pre_trip_checklist")
    private boolean requirePreTripChecklist;

    @Column(name = "fleet_gps_position_interval_seconds")
    private int gpsPositionIntervalSeconds;

    @Column(name = "fleet_default_vehicle_pallet_capacity")
    private int defaultVehiclePalletCapacity;

    public FleetConfigurationEmbeddable() {
    }

    public static FleetConfigurationEmbeddable from(final FleetConfiguration configuration) {
        final FleetConfiguration source = configuration != null
                ? configuration
                : FleetConfiguration.defaultConfiguration();
        final FleetConfigurationEmbeddable embeddable = new FleetConfigurationEmbeddable();
        embeddable.vehicleInspectionReminderDays = source.getVehicleInspectionReminderDays();
        embeddable.vehicleInsuranceReminderDays = source.getVehicleInsuranceReminderDays();
        embeddable.maxDriverShiftHours = source.getMaxDriverShiftHours();
        embeddable.requiredBreakMinutes = source.getRequiredBreakMinutes();
        embeddable.requirePreTripChecklist = source.isRequirePreTripChecklist();
        embeddable.gpsPositionIntervalSeconds = source.getGpsPositionIntervalSeconds();
        embeddable.defaultVehiclePalletCapacity = source.getDefaultVehiclePalletCapacity();
        return embeddable;
    }

    public FleetConfiguration toModel() {
        return new FleetConfiguration(
                vehicleInspectionReminderDays,
                vehicleInsuranceReminderDays,
                maxDriverShiftHours,
                requiredBreakMinutes,
                requirePreTripChecklist,
                gpsPositionIntervalSeconds,
                defaultVehiclePalletCapacity
        );
    }
}
