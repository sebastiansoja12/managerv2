package com.warehouse.shipment.domain.vo.conf;

import java.util.Objects;

public record OperatorShipmentConfiguration(
        ShipmentValidationRules validationRules,
        ShipmentLabelSettings labelSettings,
        ShipmentLimits limits,
        ShipmentWorkflowSettings workflowSettings,
        TrackingNumberRule trackingNumberRule,
        ShipmentNotificationSettings notificationSettings
) {

    private static final OperatorShipmentConfiguration DEFAULT = new OperatorShipmentConfiguration(
            ShipmentValidationRules.defaults(),
            ShipmentLabelSettings.defaults(),
            ShipmentLimits.defaults(),
            ShipmentWorkflowSettings.defaults(),
            TrackingNumberRule.defaults(),
            ShipmentNotificationSettings.defaults()
    );

    public OperatorShipmentConfiguration {
        validationRules = Objects.requireNonNullElse(validationRules, ShipmentValidationRules.defaults());
        labelSettings = Objects.requireNonNullElse(labelSettings, ShipmentLabelSettings.defaults());
        limits = Objects.requireNonNullElse(limits, ShipmentLimits.defaults());
        workflowSettings = Objects.requireNonNullElse(workflowSettings, ShipmentWorkflowSettings.defaults());
        trackingNumberRule = Objects.requireNonNullElse(trackingNumberRule, TrackingNumberRule.defaults());
        notificationSettings = Objects.requireNonNullElse(notificationSettings, ShipmentNotificationSettings.defaults());
    }

    public static OperatorShipmentConfiguration defaults() {
        return DEFAULT;
    }
}
