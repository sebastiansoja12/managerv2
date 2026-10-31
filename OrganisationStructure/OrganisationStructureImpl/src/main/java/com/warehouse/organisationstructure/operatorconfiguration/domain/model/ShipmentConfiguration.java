package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentConfiguration {
    private ShipmentValidationConfiguration validationConfiguration;
    private ShipmentLabelConfiguration labelConfiguration;
    private ShipmentLimits shipmentLimits;
    private ShipmentWorkflowConfiguration workflowConfiguration;
    private TrackingNumberRule trackingNumberRule;
    private ShipmentNotificationConfiguration notificationConfiguration;

    public ShipmentConfiguration() {
    }

    public ShipmentConfiguration(final ShipmentValidationConfiguration validationConfiguration,
                                 final ShipmentLabelConfiguration labelConfiguration,
                                 final ShipmentLimits shipmentLimits,
                                 final ShipmentWorkflowConfiguration workflowConfiguration,
                                 final TrackingNumberRule trackingNumberRule,
                                 final ShipmentNotificationConfiguration notificationConfiguration) {
        this.validationConfiguration = validationConfiguration;
        this.labelConfiguration = labelConfiguration;
        this.shipmentLimits = shipmentLimits;
        this.workflowConfiguration = workflowConfiguration;
        this.trackingNumberRule = trackingNumberRule;
        this.notificationConfiguration = notificationConfiguration;
    }

    public static ShipmentConfiguration defaultConfiguration() {
        return defaultWith(new ShipmentLimits(31.5, 0.0, 120.0, 80.0, 80.0, 0.0, false));
    }

    public static ShipmentConfiguration defaultWith(final ShipmentLimits shipmentLimits) {
        return new ShipmentConfiguration(
                ShipmentValidationConfiguration.defaultConfiguration(),
                ShipmentLabelConfiguration.defaultConfiguration(),
                shipmentLimits != null ? shipmentLimits : new ShipmentLimits(),
                ShipmentWorkflowConfiguration.defaultConfiguration(),
                TrackingNumberRule.defaultRule(),
                ShipmentNotificationConfiguration.defaultConfiguration()
        );
    }

    public ShipmentValidationConfiguration getValidationConfiguration() {
        return validationConfiguration;
    }

    public ShipmentLabelConfiguration getLabelConfiguration() {
        return labelConfiguration;
    }

    public ShipmentLimits getShipmentLimits() {
        return shipmentLimits;
    }

    public ShipmentWorkflowConfiguration getWorkflowConfiguration() {
        return workflowConfiguration;
    }

    public TrackingNumberRule getTrackingNumberRule() {
        return trackingNumberRule;
    }

    public ShipmentNotificationConfiguration getNotificationConfiguration() {
        return notificationConfiguration;
    }

    public ShipmentConfiguration withValidationConfiguration(final ShipmentValidationConfiguration validationConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration);
    }

    public ShipmentConfiguration withLabelConfiguration(final ShipmentLabelConfiguration labelConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration);
    }

    public ShipmentConfiguration withShipmentLimits(final ShipmentLimits shipmentLimits) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration);
    }

    public ShipmentConfiguration withWorkflowConfiguration(final ShipmentWorkflowConfiguration workflowConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration);
    }

    public ShipmentConfiguration withTrackingNumberRule(final TrackingNumberRule trackingNumberRule) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration);
    }

    public ShipmentConfiguration withNotificationConfiguration(final ShipmentNotificationConfiguration notificationConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration);
    }
}
