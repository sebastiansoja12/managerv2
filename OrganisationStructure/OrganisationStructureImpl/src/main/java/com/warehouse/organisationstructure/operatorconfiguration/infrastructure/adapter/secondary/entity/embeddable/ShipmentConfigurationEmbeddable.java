package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentConfiguration;
import jakarta.persistence.Embedded;
import jakarta.persistence.Embeddable;

@Embeddable
public class ShipmentConfigurationEmbeddable {

    @Embedded
    private ShipmentValidationConfigurationEmbeddable validationConfiguration;

    @Embedded
    private ShipmentLabelConfigurationEmbeddable labelConfiguration;

    @Embedded
    private ShipmentLimitsEmbeddable shipmentLimits;

    @Embedded
    private ShipmentWorkflowConfigurationEmbeddable workflowConfiguration;

    @Embedded
    private TrackingNumberRuleEmbeddable trackingNumberRule;

    @Embedded
    private ShipmentNotificationConfigurationEmbeddable notificationConfiguration;

    public ShipmentConfigurationEmbeddable() {
    }

    public static ShipmentConfigurationEmbeddable from(
            final ShipmentConfiguration configuration) {
        final ShipmentConfiguration source = configuration != null
                ? configuration
                : ShipmentConfiguration.defaultConfiguration();
        final ShipmentConfigurationEmbeddable embeddable = new ShipmentConfigurationEmbeddable();
        embeddable.validationConfiguration = ShipmentValidationConfigurationEmbeddable.from(
                source.getValidationConfiguration());
        embeddable.labelConfiguration = ShipmentLabelConfigurationEmbeddable.from(source.getLabelConfiguration());
        embeddable.shipmentLimits = ShipmentLimitsEmbeddable.from(source.getShipmentLimits());
        embeddable.workflowConfiguration = ShipmentWorkflowConfigurationEmbeddable.from(
                source.getWorkflowConfiguration());
        embeddable.trackingNumberRule = TrackingNumberRuleEmbeddable.from(source.getTrackingNumberRule());
        embeddable.notificationConfiguration = ShipmentNotificationConfigurationEmbeddable.from(
                source.getNotificationConfiguration());
        return embeddable;
    }

    public ShipmentConfiguration toModel() {
        return new ShipmentConfiguration(
                validationConfiguration != null ? validationConfiguration.toModel() : null,
                labelConfiguration != null ? labelConfiguration.toModel() : null,
                shipmentLimits != null ? shipmentLimits.toModel() : null,
                workflowConfiguration != null ? workflowConfiguration.toModel() : null,
                trackingNumberRule != null ? trackingNumberRule.toModel() : null,
                notificationConfiguration != null ? notificationConfiguration.toModel() : null
        );
    }
}
