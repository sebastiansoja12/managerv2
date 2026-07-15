package com.warehouse.organisationstructure.api.dto;

public record ShipmentConfigurationDto(
        ShipmentValidationConfigurationDto validationConfiguration,
        ShipmentLabelConfigurationDto labelConfiguration,
        ShipmentLimitsDto shipmentLimits,
        ShipmentWorkflowConfigurationDto workflowConfiguration,
        TrackingNumberRuleDto trackingNumberRule,
        ShipmentNotificationConfigurationDto notificationConfiguration
) {
}
