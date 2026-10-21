package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary.api;

import java.time.LocalDate;

public record CurrentSubscriptionApiResponse(String plan,
                                             LocalDate startDate,
                                             LocalDate endDate,
                                             String status,
                                             boolean accessAllowed) {
}
