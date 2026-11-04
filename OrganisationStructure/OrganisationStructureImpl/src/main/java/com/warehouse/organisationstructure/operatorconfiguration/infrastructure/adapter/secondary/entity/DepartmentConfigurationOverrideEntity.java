package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DeliveryAreaMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentWorkTransferMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.UncoveredPostalCodePolicy;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity(name = "operatorconfiguration.DepartmentConfigurationOverrideEntity")
@Table(name = "department_configuration_override")
public class DepartmentConfigurationOverrideEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "department_id", nullable = false, updatable = false))
    private DepartmentId departmentId;

    @Column(name = "auto_assign_delivery_areas")
    private Boolean autoAssignDeliveryAreas;

    @Column(name = "auto_connect_departments")
    private Boolean autoConnectDepartments;

    @Column(name = "prevent_department_deletion")
    private Boolean preventDepartmentDeletion;

    @Column(name = "prevent_department_archiving")
    private Boolean preventDepartmentArchiving;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_area_mode", length = 32)
    private DeliveryAreaMode deliveryAreaMode;

    @Column(name = "max_delivery_radius_km")
    private Integer maxDeliveryRadiusKm;

    @Column(name = "allow_manual_delivery_area_edit")
    private Boolean allowManualDeliveryAreaEdit;

    @Column(name = "recalculate_areas_on_status_change")
    private Boolean recalculateAreasOnStatusChange;

    @Enumerated(EnumType.STRING)
    @Column(name = "uncovered_postal_code_policy", length = 32)
    private UncoveredPostalCodePolicy uncoveredPostalCodePolicy;

    @Column(name = "sorting_center_connection_count")
    private Integer sortingCenterConnectionCount;

    @Column(name = "max_auto_connection_distance_km")
    private Integer maxAutoConnectionDistanceKm;

    @Column(name = "require_sorting_connection_for_activation")
    private Boolean requireSortingConnectionForActivation;

    @Column(name = "bidirectional_connections_by_default")
    private Boolean bidirectionalConnectionsByDefault;

    @Column(name = "require_status_change_reason")
    private Boolean requireStatusChangeReason;

    @Column(name = "block_deactivation_with_active_work")
    private Boolean blockDeactivationWithActiveWork;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_transfer_on_deactivation", length = 32)
    private DepartmentWorkTransferMode workTransferOnDeactivation;

    @Column(name = "auto_archive_inactive_after_days")
    private Integer autoArchiveInactiveAfterDays;

    @Column(name = "daily_capacity")
    private Integer dailyCapacity;

    @Column(name = "capacity_warning_threshold_percent")
    private Integer capacityWarningThresholdPercent;

    @Column(name = "redirect_overflow_to_neighbour")
    private Boolean redirectOverflowToNeighbour;

    @Column(name = "max_couriers_per_department")
    private Integer maxCouriersPerDepartment;

    protected DepartmentConfigurationOverrideEntity() {
    }

    public DepartmentConfigurationOverrideEntity(final DepartmentId departmentId,
                                                 final Boolean autoAssignDeliveryAreas,
                                                 final Boolean autoConnectDepartments,
                                                 final Boolean preventDepartmentDeletion,
                                                 final Boolean preventDepartmentArchiving,
                                                 final DeliveryAreaMode deliveryAreaMode,
                                                 final Integer maxDeliveryRadiusKm,
                                                 final Boolean allowManualDeliveryAreaEdit,
                                                 final Boolean recalculateAreasOnStatusChange,
                                                 final UncoveredPostalCodePolicy uncoveredPostalCodePolicy,
                                                 final Integer sortingCenterConnectionCount,
                                                 final Integer maxAutoConnectionDistanceKm,
                                                 final Boolean requireSortingConnectionForActivation,
                                                 final Boolean bidirectionalConnectionsByDefault,
                                                 final Boolean requireStatusChangeReason,
                                                 final Boolean blockDeactivationWithActiveWork,
                                                 final DepartmentWorkTransferMode workTransferOnDeactivation,
                                                 final Integer autoArchiveInactiveAfterDays,
                                                 final Integer dailyCapacity,
                                                 final Integer capacityWarningThresholdPercent,
                                                 final Boolean redirectOverflowToNeighbour,
                                                 final Integer maxCouriersPerDepartment) {
        this.departmentId = departmentId;
        this.autoAssignDeliveryAreas = autoAssignDeliveryAreas;
        this.autoConnectDepartments = autoConnectDepartments;
        this.preventDepartmentDeletion = preventDepartmentDeletion;
        this.preventDepartmentArchiving = preventDepartmentArchiving;
        this.deliveryAreaMode = deliveryAreaMode;
        this.maxDeliveryRadiusKm = maxDeliveryRadiusKm;
        this.allowManualDeliveryAreaEdit = allowManualDeliveryAreaEdit;
        this.recalculateAreasOnStatusChange = recalculateAreasOnStatusChange;
        this.uncoveredPostalCodePolicy = uncoveredPostalCodePolicy;
        this.sortingCenterConnectionCount = sortingCenterConnectionCount;
        this.maxAutoConnectionDistanceKm = maxAutoConnectionDistanceKm;
        this.requireSortingConnectionForActivation = requireSortingConnectionForActivation;
        this.bidirectionalConnectionsByDefault = bidirectionalConnectionsByDefault;
        this.requireStatusChangeReason = requireStatusChangeReason;
        this.blockDeactivationWithActiveWork = blockDeactivationWithActiveWork;
        this.workTransferOnDeactivation = workTransferOnDeactivation;
        this.autoArchiveInactiveAfterDays = autoArchiveInactiveAfterDays;
        this.dailyCapacity = dailyCapacity;
        this.capacityWarningThresholdPercent = capacityWarningThresholdPercent;
        this.redirectOverflowToNeighbour = redirectOverflowToNeighbour;
        this.maxCouriersPerDepartment = maxCouriersPerDepartment;
    }
}
