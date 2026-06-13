package com.warehouse.department.infrastructure.adapter.secondary.event;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.kafka.infrastructure.annotation.KafkaTopic;

@KafkaTopic("${manager.kafka.topics.department-user-events:department.user.events}")
public record DepartmentUserDeleted(DepartmentCode departmentCode,
                                    Instant timestamp) {
}
