package com.warehouse.shipment.domain.vo.conf;

import java.util.Objects;

public record OperatorShipmentConfiguration(
        ShipmentValidationRules validationRules,
        ShipmentLabelSettings labelSettings,
        ShipmentLimits limits,
        ShipmentWorkflowSettings workflowSettings,
        TrackingNumberRule trackingNumberRule,
        ShipmentNotificationSettings notificationSettings,
        boolean paymentsEnabled
) {

    private static final OperatorShipmentConfiguration DEFAULT = new OperatorShipmentConfiguration(
            ShipmentValidationRules.defaults(),
            ShipmentLabelSettings.defaults(),
            ShipmentLimits.defaults(),
            ShipmentWorkflowSettings.defaults(),
            TrackingNumberRule.defaults(),
            ShipmentNotificationSettings.defaults(),
            false
    );

    public OperatorShipmentConfiguration {
        validationRules = Objects.requireNonNullElse(validationRules, ShipmentValidationRules.defaults());
        labelSettings = Objects.requireNonNullElse(labelSettings, ShipmentLabelSettings.defaults());
        limits = Objects.requireNonNullElse(limits, ShipmentLimits.defaults());
        workflowSettings = Objects.requireNonNullElse(workflowSettings, ShipmentWorkflowSettings.defaults());
        trackingNumberRule = Objects.requireNonNullElse(trackingNumberRule, TrackingNumberRule.defaults());
        notificationSettings = Objects.requireNonNullElse(notificationSettings, ShipmentNotificationSettings.defaults());
    }

    public OperatorShipmentConfiguration(final ShipmentValidationRules validationRules,
                                         final ShipmentLabelSettings labelSettings,
                                         final ShipmentLimits limits,
                                         final ShipmentWorkflowSettings workflowSettings,
                                         final TrackingNumberRule trackingNumberRule,
                                         final ShipmentNotificationSettings notificationSettings) {
        this(validationRules, labelSettings, limits, workflowSettings, trackingNumberRule, notificationSettings,
                false);
    }

    public static OperatorShipmentConfiguration defaults() {
        return DEFAULT;
    }
}
