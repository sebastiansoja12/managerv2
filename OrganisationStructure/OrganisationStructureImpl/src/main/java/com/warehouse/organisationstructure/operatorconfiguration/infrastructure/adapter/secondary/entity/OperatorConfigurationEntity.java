package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CommunicationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.FleetConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RegionalConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.SortingConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.CommunicationConfigurationEmbeddable;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.DeliveryTimeConfigurationEmbeddable;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.DepartmentConfigurationEmbeddable;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.FleetConfigurationEmbeddable;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.PickupPointConfigurationEmbeddable;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.RegionalConfigurationEmbeddable;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.ShipmentConfigurationEmbeddable;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.ShippingCapabilitiesEmbeddable;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable.SortingConfigurationEmbeddable;
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

    @Embedded
    private DepartmentConfigurationEmbeddable departmentConfiguration;

    @Embedded
    private FleetConfigurationEmbeddable fleetConfiguration;

    @Embedded
    private CommunicationConfigurationEmbeddable communicationConfiguration;

    @Embedded
    private RegionalConfigurationEmbeddable regionalConfiguration;

    @Embedded
    private SortingConfigurationEmbeddable sortingConfiguration;

    @Embedded
    private PickupPointConfigurationEmbeddable pickupPointConfiguration;

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
        entity.departmentConfiguration = DepartmentConfigurationEmbeddable.from(source.getDepartmentConfiguration());
        entity.fleetConfiguration = FleetConfigurationEmbeddable.from(source.getFleetConfiguration());
        entity.communicationConfiguration = CommunicationConfigurationEmbeddable.from(
                source.getCommunicationConfiguration());
        entity.regionalConfiguration = RegionalConfigurationEmbeddable.from(source.getRegionalConfiguration());
        entity.sortingConfiguration = SortingConfigurationEmbeddable.from(source.getSortingConfiguration());
        entity.pickupPointConfiguration = PickupPointConfigurationEmbeddable.from(
                source.getPickupPointConfiguration());
        return entity;
    }

    public OperatorConfiguration toModel() {
        return new OperatorConfiguration(
                shippingCapabilities != null ? shippingCapabilities.toModel() : null,
                shipmentConfiguration != null ? shipmentConfiguration.toModel() : null,
                deliveryTimeConfiguration != null ? deliveryTimeConfiguration.toModel() : null,
                departmentConfiguration != null ? departmentConfiguration.toModel()
                        : DepartmentConfiguration.defaultConfiguration(),
                fleetConfiguration != null ? fleetConfiguration.toModel() : FleetConfiguration.defaultConfiguration(),
                communicationConfiguration != null ? communicationConfiguration.toModel()
                        : CommunicationConfiguration.defaultConfiguration(),
                regionalConfiguration != null ? regionalConfiguration.toModel()
                        : RegionalConfiguration.defaultConfiguration(),
                sortingConfiguration != null ? sortingConfiguration.toModel()
                        : SortingConfiguration.defaultConfiguration(),
                pickupPointConfiguration != null ? pickupPointConfiguration.toModel()
                        : PickupPointConfiguration.defaultConfiguration()
        );
    }
}
