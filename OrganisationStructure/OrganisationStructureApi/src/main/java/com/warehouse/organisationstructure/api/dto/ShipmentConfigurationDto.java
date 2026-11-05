package com.warehouse.organisationstructure.api.dto;

public record ShipmentConfigurationDto(
        ShipmentValidationConfigurationDto validationConfiguration,
        ShipmentLabelConfigurationDto labelConfiguration,
        ShipmentLimitsDto shipmentLimits,
        ShipmentWorkflowConfigurationDto workflowConfiguration,
        TrackingNumberRuleDto trackingNumberRule,
        ShipmentNotificationConfigurationDto notificationConfiguration,
        ShipmentDeliveryConfigurationDto deliveryConfiguration,
        ShipmentReturnConfigurationDto returnConfiguration,
        ShipmentPaymentConfigurationDto paymentConfiguration,
        ShipmentSlaConfigurationDto slaConfiguration,
        ShipmentDataRetentionConfigurationDto dataRetentionConfiguration
) {

    public ShipmentConfigurationDto(final ShipmentValidationConfigurationDto validationConfiguration,
                                    final ShipmentLabelConfigurationDto labelConfiguration,
                                    final ShipmentLimitsDto shipmentLimits,
                                    final ShipmentWorkflowConfigurationDto workflowConfiguration,
                                    final TrackingNumberRuleDto trackingNumberRule,
                                    final ShipmentNotificationConfigurationDto notificationConfiguration) {
        this(validationConfiguration, labelConfiguration, shipmentLimits, workflowConfiguration, trackingNumberRule,
                notificationConfiguration, null, null, null, null, null);
    }
}
