package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class DepartmentConfigurationEmbeddable {

    @Column(name = "department_auto_assign_delivery_areas")
    private Boolean autoAssignDeliveryAreas;

    @Column(name = "department_auto_connect")
    private Boolean autoConnectDepartments;

    @Column(name = "department_prevent_deletion")
    private Boolean preventDepartmentDeletion;

    @Column(name = "department_prevent_archiving")
    private Boolean preventDepartmentArchiving;

    @Embedded
    private DepartmentDeliveryAreaPolicyEmbeddable deliveryAreaPolicy;

    @Embedded
    private DepartmentConnectionPolicyEmbeddable connectionPolicy;

    @Embedded
    private DepartmentLifecyclePolicyEmbeddable lifecyclePolicy;

    @Embedded
    private DepartmentCapacityPolicyEmbeddable capacityPolicy;

    public DepartmentConfigurationEmbeddable() {
    }

    public static DepartmentConfigurationEmbeddable from(final DepartmentConfiguration configuration) {
        final DepartmentConfiguration source = configuration != null
                ? configuration
                : DepartmentConfiguration.defaultConfiguration();
        final DepartmentConfigurationEmbeddable embeddable = new DepartmentConfigurationEmbeddable();
        embeddable.autoAssignDeliveryAreas = source.isAutoAssignDeliveryAreas();
        embeddable.autoConnectDepartments = source.isAutoConnectDepartments();
        embeddable.preventDepartmentDeletion = source.isPreventDepartmentDeletion();
        embeddable.preventDepartmentArchiving = source.isPreventDepartmentArchiving();
        embeddable.deliveryAreaPolicy = DepartmentDeliveryAreaPolicyEmbeddable.from(source.getDeliveryAreaPolicy());
        embeddable.connectionPolicy = DepartmentConnectionPolicyEmbeddable.from(source.getConnectionPolicy());
        embeddable.lifecyclePolicy = DepartmentLifecyclePolicyEmbeddable.from(source.getLifecyclePolicy());
        embeddable.capacityPolicy = DepartmentCapacityPolicyEmbeddable.from(source.getCapacityPolicy());
        return embeddable;
    }

    public DepartmentConfiguration toModel() {
        return new DepartmentConfiguration(
                Boolean.TRUE.equals(autoAssignDeliveryAreas),
                Boolean.TRUE.equals(autoConnectDepartments),
                Boolean.TRUE.equals(preventDepartmentDeletion),
                Boolean.TRUE.equals(preventDepartmentArchiving),
                deliveryAreaPolicy != null ? deliveryAreaPolicy.toModel() : null,
                connectionPolicy != null ? connectionPolicy.toModel() : null,
                lifecyclePolicy != null ? lifecyclePolicy.toModel() : null,
                capacityPolicy != null ? capacityPolicy.toModel() : null
        );
    }
}
