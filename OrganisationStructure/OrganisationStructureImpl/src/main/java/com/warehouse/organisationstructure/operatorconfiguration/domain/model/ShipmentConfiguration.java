package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentConfiguration {
    private ShipmentValidationConfiguration validationConfiguration;
    private ShipmentLabelConfiguration labelConfiguration;
    private ShipmentLimits shipmentLimits;
    private ShipmentWorkflowConfiguration workflowConfiguration;
    private TrackingNumberRule trackingNumberRule;
    private ShipmentNotificationConfiguration notificationConfiguration;
    private ShipmentDeliveryConfiguration deliveryConfiguration;
    private ShipmentReturnConfiguration returnConfiguration;
    private ShipmentPaymentConfiguration paymentConfiguration;
    private ShipmentSlaConfiguration slaConfiguration;
    private ShipmentDataRetentionConfiguration dataRetentionConfiguration;

    public ShipmentConfiguration() {
    }

    public ShipmentConfiguration(final ShipmentValidationConfiguration validationConfiguration,
                                 final ShipmentLabelConfiguration labelConfiguration,
                                 final ShipmentLimits shipmentLimits,
                                 final ShipmentWorkflowConfiguration workflowConfiguration,
                                 final TrackingNumberRule trackingNumberRule,
                                 final ShipmentNotificationConfiguration notificationConfiguration) {
        this(validationConfiguration, labelConfiguration, shipmentLimits, workflowConfiguration, trackingNumberRule,
                notificationConfiguration,
                ShipmentDeliveryConfiguration.defaultConfiguration(),
                ShipmentReturnConfiguration.defaultConfiguration(),
                ShipmentPaymentConfiguration.defaultConfiguration(),
                ShipmentSlaConfiguration.defaultConfiguration(),
                ShipmentDataRetentionConfiguration.defaultConfiguration());
    }

    public ShipmentConfiguration(final ShipmentValidationConfiguration validationConfiguration,
                                 final ShipmentLabelConfiguration labelConfiguration,
                                 final ShipmentLimits shipmentLimits,
                                 final ShipmentWorkflowConfiguration workflowConfiguration,
                                 final TrackingNumberRule trackingNumberRule,
                                 final ShipmentNotificationConfiguration notificationConfiguration,
                                 final ShipmentDeliveryConfiguration deliveryConfiguration,
                                 final ShipmentReturnConfiguration returnConfiguration,
                                 final ShipmentPaymentConfiguration paymentConfiguration,
                                 final ShipmentSlaConfiguration slaConfiguration,
                                 final ShipmentDataRetentionConfiguration dataRetentionConfiguration) {
        this.validationConfiguration = validationConfiguration;
        this.labelConfiguration = labelConfiguration;
        this.shipmentLimits = shipmentLimits;
        this.workflowConfiguration = workflowConfiguration;
        this.trackingNumberRule = trackingNumberRule;
        this.notificationConfiguration = notificationConfiguration;
        this.deliveryConfiguration = deliveryConfiguration;
        this.returnConfiguration = returnConfiguration;
        this.paymentConfiguration = paymentConfiguration;
        this.slaConfiguration = slaConfiguration;
        this.dataRetentionConfiguration = dataRetentionConfiguration;
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

    public ShipmentDeliveryConfiguration getDeliveryConfiguration() {
        return deliveryConfiguration;
    }

    public ShipmentReturnConfiguration getReturnConfiguration() {
        return returnConfiguration;
    }

    public ShipmentPaymentConfiguration getPaymentConfiguration() {
        return paymentConfiguration;
    }

    public ShipmentSlaConfiguration getSlaConfiguration() {
        return slaConfiguration;
    }

    public ShipmentDataRetentionConfiguration getDataRetentionConfiguration() {
        return dataRetentionConfiguration;
    }

    public ShipmentConfiguration withValidationConfiguration(final ShipmentValidationConfiguration validationConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration, deliveryConfiguration,
                returnConfiguration, paymentConfiguration, slaConfiguration, dataRetentionConfiguration);
    }

    public ShipmentConfiguration withLabelConfiguration(final ShipmentLabelConfiguration labelConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration, deliveryConfiguration,
                returnConfiguration, paymentConfiguration, slaConfiguration, dataRetentionConfiguration);
    }

    public ShipmentConfiguration withShipmentLimits(final ShipmentLimits shipmentLimits) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration, deliveryConfiguration,
                returnConfiguration, paymentConfiguration, slaConfiguration, dataRetentionConfiguration);
    }

    public ShipmentConfiguration withWorkflowConfiguration(final ShipmentWorkflowConfiguration workflowConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration, deliveryConfiguration,
                returnConfiguration, paymentConfiguration, slaConfiguration, dataRetentionConfiguration);
    }

    public ShipmentConfiguration withTrackingNumberRule(final TrackingNumberRule trackingNumberRule) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration, deliveryConfiguration,
                returnConfiguration, paymentConfiguration, slaConfiguration, dataRetentionConfiguration);
    }

    public ShipmentConfiguration withNotificationConfiguration(final ShipmentNotificationConfiguration notificationConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration, deliveryConfiguration,
                returnConfiguration, paymentConfiguration, slaConfiguration, dataRetentionConfiguration);
    }

    public ShipmentConfiguration withDeliveryConfiguration(final ShipmentDeliveryConfiguration deliveryConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration, deliveryConfiguration,
                returnConfiguration, paymentConfiguration, slaConfiguration, dataRetentionConfiguration);
    }

    public ShipmentConfiguration withReturnConfiguration(final ShipmentReturnConfiguration returnConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration, deliveryConfiguration,
                returnConfiguration, paymentConfiguration, slaConfiguration, dataRetentionConfiguration);
    }

    public ShipmentConfiguration withPaymentConfiguration(final ShipmentPaymentConfiguration paymentConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration, deliveryConfiguration,
                returnConfiguration, paymentConfiguration, slaConfiguration, dataRetentionConfiguration);
    }

    public ShipmentConfiguration withSlaConfiguration(final ShipmentSlaConfiguration slaConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration, deliveryConfiguration,
                returnConfiguration, paymentConfiguration, slaConfiguration, dataRetentionConfiguration);
    }

    public ShipmentConfiguration withDataRetentionConfiguration(
            final ShipmentDataRetentionConfiguration dataRetentionConfiguration) {
        return new ShipmentConfiguration(validationConfiguration, labelConfiguration, shipmentLimits,
                workflowConfiguration, trackingNumberRule, notificationConfiguration, deliveryConfiguration,
                returnConfiguration, paymentConfiguration, slaConfiguration, dataRetentionConfiguration);
    }
}
