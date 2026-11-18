package com.warehouse.mcp.domain.vo;

import java.time.LocalDateTime;

public record DeliveryConditions(String method, Boolean signatureRequired, String instructions,
                                 LocalDateTime plannedAt) {

    public DeliveryConditions applyTo(final DeliveryDetails delivery) {
        return new DeliveryConditions(
                method != null ? method : delivery.method(),
                signatureRequired != null ? signatureRequired : delivery.signatureRequired(),
                instructions != null ? instructions : delivery.instructions(),
                plannedAt != null ? plannedAt : delivery.plannedAt());
    }
}
