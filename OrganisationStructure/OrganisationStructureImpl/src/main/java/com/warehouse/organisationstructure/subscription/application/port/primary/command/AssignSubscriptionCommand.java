package com.warehouse.organisationstructure.subscription.application.port.primary.command;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.subscription.domain.enumeration.SubscriptionPlan;

import java.time.LocalDate;

public record AssignSubscriptionCommand(OperatorId operatorId,
                                        SubscriptionPlan plan,
                                        LocalDate startDate,
                                        LocalDate endDate) {
}
