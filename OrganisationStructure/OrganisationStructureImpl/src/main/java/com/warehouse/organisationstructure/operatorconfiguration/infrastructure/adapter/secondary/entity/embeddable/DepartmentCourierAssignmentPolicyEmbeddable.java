package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CourierAssignmentDateField;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CourierAssignmentWindowType;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentCourierAssignmentPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalTime;

@Embeddable
public class DepartmentCourierAssignmentPolicyEmbeddable {

    @Column(name = "department_courier_assignment_enabled")
    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "department_courier_assignment_date_field")
    private CourierAssignmentDateField dateField;

    @Enumerated(EnumType.STRING)
    @Column(name = "department_courier_assignment_window_type")
    private CourierAssignmentWindowType windowType;

    @Column(name = "department_courier_assignment_window_start")
    private LocalTime windowStart;

    @Column(name = "department_courier_assignment_window_end")
    private LocalTime windowEnd;

    @Column(name = "department_courier_assignment_time_zone")
    private String timeZone;

    @Column(name = "department_courier_assignment_processing_delay_minutes")
    private Integer processingDelayMinutes;

    @Column(name = "department_courier_assignment_overlap_minutes")
    private Integer overlapMinutes;

    @Column(name = "department_courier_assignment_max_window_hours")
    private Integer maxWindowHours;

    @Column(name = "department_courier_assignment_lookback_days")
    private Integer lookbackDays;

    @Column(name = "department_courier_assignment_batch_size")
    private Integer batchSize;

    public DepartmentCourierAssignmentPolicyEmbeddable() {
    }

    public static DepartmentCourierAssignmentPolicyEmbeddable from(
            final DepartmentCourierAssignmentPolicy configuration) {
        if (configuration == null) {
            return null;
        }
        final DepartmentCourierAssignmentPolicyEmbeddable embeddable = new DepartmentCourierAssignmentPolicyEmbeddable();
        embeddable.enabled = configuration.isEnabled();
        embeddable.dateField = configuration.getDateField();
        embeddable.windowType = configuration.getWindowType();
        embeddable.windowStart = configuration.getWindowStart();
        embeddable.windowEnd = configuration.getWindowEnd();
        embeddable.timeZone = configuration.getTimeZone();
        embeddable.processingDelayMinutes = configuration.getProcessingDelayMinutes();
        embeddable.overlapMinutes = configuration.getOverlapMinutes();
        embeddable.maxWindowHours = configuration.getMaxWindowHours();
        embeddable.lookbackDays = configuration.getLookbackDays();
        embeddable.batchSize = configuration.getBatchSize();
        return embeddable;
    }

    public DepartmentCourierAssignmentPolicy toModel() {
        if (enabled == null) {
            return null;
        }
        final DepartmentCourierAssignmentPolicy defaults = DepartmentCourierAssignmentPolicy.defaultConfiguration();
        return new DepartmentCourierAssignmentPolicy(
                enabled,
                dateField,
                windowType,
                windowStart,
                windowEnd,
                timeZone,
                processingDelayMinutes != null ? processingDelayMinutes : defaults.getProcessingDelayMinutes(),
                overlapMinutes != null ? overlapMinutes : defaults.getOverlapMinutes(),
                maxWindowHours != null ? maxWindowHours : defaults.getMaxWindowHours(),
                lookbackDays != null ? lookbackDays : defaults.getLookbackDays(),
                batchSize != null ? batchSize : defaults.getBatchSize()
        );
    }
}
