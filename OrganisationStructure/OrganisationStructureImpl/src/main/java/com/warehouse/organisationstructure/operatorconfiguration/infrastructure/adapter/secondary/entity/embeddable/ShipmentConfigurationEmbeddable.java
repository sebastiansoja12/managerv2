package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentDeliveryConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentReturnConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentPaymentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentSlaConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentDataRetentionConfiguration;
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

    @Embedded
    private ShipmentDeliveryConfigurationEmbeddable deliveryConfiguration;

    @Embedded
    private ShipmentReturnConfigurationEmbeddable returnConfiguration;

    @Embedded
    private ShipmentPaymentConfigurationEmbeddable paymentConfiguration;

    @Embedded
    private ShipmentSlaConfigurationEmbeddable slaConfiguration;

    @Embedded
    private ShipmentDataRetentionConfigurationEmbeddable dataRetentionConfiguration;

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
        embeddable.deliveryConfiguration = ShipmentDeliveryConfigurationEmbeddable.from(source.getDeliveryConfiguration());
        embeddable.returnConfiguration = ShipmentReturnConfigurationEmbeddable.from(source.getReturnConfiguration());
        embeddable.paymentConfiguration = ShipmentPaymentConfigurationEmbeddable.from(source.getPaymentConfiguration());
        embeddable.slaConfiguration = ShipmentSlaConfigurationEmbeddable.from(source.getSlaConfiguration());
        embeddable.dataRetentionConfiguration = ShipmentDataRetentionConfigurationEmbeddable.from(source.getDataRetentionConfiguration());
        return embeddable;
    }

    public ShipmentConfiguration toModel() {
        return new ShipmentConfiguration(
                validationConfiguration != null ? validationConfiguration.toModel() : null,
                labelConfiguration != null ? labelConfiguration.toModel() : null,
                shipmentLimits != null ? shipmentLimits.toModel() : null,
                workflowConfiguration != null ? workflowConfiguration.toModel() : null,
                trackingNumberRule != null ? trackingNumberRule.toModel() : null,
                notificationConfiguration != null ? notificationConfiguration.toModel() : null,
                deliveryConfiguration != null ? deliveryConfiguration.toModel() : ShipmentDeliveryConfiguration.defaultConfiguration(),
                returnConfiguration != null ? returnConfiguration.toModel() : ShipmentReturnConfiguration.defaultConfiguration(),
                paymentConfiguration != null ? paymentConfiguration.toModel() : ShipmentPaymentConfiguration.defaultConfiguration(),
                slaConfiguration != null ? slaConfiguration.toModel() : ShipmentSlaConfiguration.defaultConfiguration(),
                dataRetentionConfiguration != null ? dataRetentionConfiguration.toModel() : ShipmentDataRetentionConfiguration.defaultConfiguration()
        );
    }
}
