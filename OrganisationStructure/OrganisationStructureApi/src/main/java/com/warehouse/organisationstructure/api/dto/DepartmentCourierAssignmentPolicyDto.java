package com.warehouse.organisationstructure.api.dto;

import java.time.LocalTime;

public record DepartmentCourierAssignmentPolicyDto(
        boolean enabled,
        CourierAssignmentDateFieldDto dateField,
        CourierAssignmentWindowTypeDto windowType,
        LocalTime windowStart,
        LocalTime windowEnd,
        String timeZone,
        int processingDelayMinutes,
        int overlapMinutes,
        int maxWindowHours,
        int lookbackDays,
        int batchSize
) {
}
