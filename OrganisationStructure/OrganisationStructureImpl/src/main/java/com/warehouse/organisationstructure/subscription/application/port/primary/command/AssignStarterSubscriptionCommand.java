package com.warehouse.organisationstructure.subscription.application.port.primary.command;

import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Instant;

public record AssignStarterSubscriptionCommand(OperatorId operatorId, Instant operatorCreatedAt) {
}
