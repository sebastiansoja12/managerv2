package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

import java.time.LocalTime;
import java.time.ZoneId;

public class DepartmentCourierAssignmentPolicy {

    private final boolean enabled;
    private final CourierAssignmentDateField dateField;
    private final CourierAssignmentWindowType windowType;
    private final LocalTime windowStart;
    private final LocalTime windowEnd;
    private final String timeZone;
    private final int processingDelayMinutes;
    private final int overlapMinutes;
    private final int maxWindowHours;
    private final int lookbackDays;
    private final int batchSize;

    public DepartmentCourierAssignmentPolicy(final boolean enabled,
                                             final CourierAssignmentDateField dateField,
                                             final CourierAssignmentWindowType windowType,
                                             final LocalTime windowStart,
                                             final LocalTime windowEnd,
                                             final String timeZone,
                                             final int processingDelayMinutes,
                                             final int overlapMinutes,
                                             final int maxWindowHours,
                                             final int lookbackDays,
                                             final int batchSize) {
        if (processingDelayMinutes < 0 || overlapMinutes < 0) {
            throw new IllegalArgumentException("Courier assignment delay and overlap must not be negative");
        }
        if (maxWindowHours < 1 || lookbackDays < 1 || batchSize < 1) {
            throw new IllegalArgumentException("Courier assignment window, lookback and batch size must be positive");
        }
        if (timeZone != null && !timeZone.isBlank()) {
            ZoneId.of(timeZone);
        }
        this.enabled = enabled;
        this.dateField = dateField != null ? dateField : CourierAssignmentDateField.DESTINATION_ARRIVAL;
        this.windowType = windowType != null ? windowType : CourierAssignmentWindowType.ROLLING;
        this.windowStart = windowStart != null ? windowStart : LocalTime.MIDNIGHT;
        this.windowEnd = windowEnd != null ? windowEnd : LocalTime.MIDNIGHT;
        this.timeZone = timeZone == null || timeZone.isBlank() ? null : timeZone;
        this.processingDelayMinutes = processingDelayMinutes;
        this.overlapMinutes = overlapMinutes;
        this.maxWindowHours = maxWindowHours;
        this.lookbackDays = lookbackDays;
        this.batchSize = batchSize;
    }

    public static DepartmentCourierAssignmentPolicy defaultConfiguration() {
        return new DepartmentCourierAssignmentPolicy(
                false,
                CourierAssignmentDateField.DESTINATION_ARRIVAL,
                CourierAssignmentWindowType.ROLLING,
                LocalTime.MIDNIGHT,
                LocalTime.MIDNIGHT,
                null,
                5,
                15,
                24,
                3,
                500
        );
    }

    public boolean isEnabled() { return enabled; }
    public CourierAssignmentDateField getDateField() { return dateField; }
    public CourierAssignmentWindowType getWindowType() { return windowType; }
    public LocalTime getWindowStart() { return windowStart; }
    public LocalTime getWindowEnd() { return windowEnd; }
    public String getTimeZone() { return timeZone; }
    public int getProcessingDelayMinutes() { return processingDelayMinutes; }
    public int getOverlapMinutes() { return overlapMinutes; }
    public int getMaxWindowHours() { return maxWindowHours; }
    public int getLookbackDays() { return lookbackDays; }
    public int getBatchSize() { return batchSize; }
}
