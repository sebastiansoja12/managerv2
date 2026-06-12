package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.DeliveryTimeConfigurationEmbeddable;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.ShipmentConfigurationEmbeddable;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.ShippingCapabilitiesEmbeddable;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

@Entity(name = "organisationstructure.OperatorConfigurationEntity")
@Table(name = "operator_configurations")
@Audited
public class OperatorConfigurationEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "operator_id", nullable = false))
    private OperatorId operatorId;

    @Embedded
    private ShippingCapabilitiesEmbeddable shippingCapabilities;

    @Embedded
    private ShipmentConfigurationEmbeddable shipmentConfiguration;

    @Embedded
    private DeliveryTimeConfigurationEmbeddable deliveryTimeConfiguration;

    public OperatorConfigurationEntity() {
    }

    public static OperatorConfigurationEntity fromModel(final OperatorId operatorId,
                                                        final OperatorConfiguration configuration) {
        final OperatorConfiguration source = configuration != null
                ? configuration
                : OperatorConfiguration.defaultFor(false, false, false);
        return fromConfiguration(operatorId, source);
    }

    public static OperatorConfigurationEntity defaultFor(final OperatorId operatorId,
                                                         final boolean supportsInternationalShipping,
                                                         final boolean supportsCashOnDelivery,
                                                         final boolean supportsLockers) {
        return fromConfiguration(operatorId, OperatorConfiguration.defaultFor(
                supportsInternationalShipping,
                supportsCashOnDelivery,
                supportsLockers
        ));
    }

    private static OperatorConfigurationEntity fromConfiguration(final OperatorId operatorId,
                                                                final OperatorConfiguration source) {
        final OperatorConfigurationEntity entity = new OperatorConfigurationEntity();
        entity.operatorId = operatorId;
        entity.shippingCapabilities = ShippingCapabilitiesEmbeddable.from(source.getShippingCapabilities());
        entity.shipmentConfiguration = ShipmentConfigurationEmbeddable.from(source.getShipmentConfiguration());
        entity.deliveryTimeConfiguration = DeliveryTimeConfigurationEmbeddable.from(
                source.getDeliveryTimeConfiguration());
        return entity;
    }

    public OperatorConfiguration toModel() {
        return new OperatorConfiguration(
                shippingCapabilities != null ? shippingCapabilities.toModel() : null,
                shipmentConfiguration != null ? shipmentConfiguration.toModel() : null,
                deliveryTimeConfiguration != null ? deliveryTimeConfiguration.toModel() : null
        );
    }
}
