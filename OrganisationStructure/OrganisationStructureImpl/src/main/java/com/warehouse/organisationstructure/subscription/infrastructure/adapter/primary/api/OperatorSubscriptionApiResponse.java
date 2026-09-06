package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Instant;
import java.time.LocalDate;

public record OperatorSubscriptionApiResponse(OperatorId operatorId,
                                              String plan,
                                              LocalDate startDate,
                                              LocalDate endDate,
                                              String status,
                                              String updatedBy,
                                              Instant updatedAt) {
}
