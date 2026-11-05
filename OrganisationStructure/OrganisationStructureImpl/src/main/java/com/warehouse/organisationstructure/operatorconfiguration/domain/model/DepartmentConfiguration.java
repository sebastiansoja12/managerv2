package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class DepartmentConfiguration {

    private final boolean autoAssignDeliveryAreas;
    private final boolean autoConnectDepartments;
    private final boolean preventDepartmentDeletion;
    private final boolean preventDepartmentArchiving;
    private final DepartmentDeliveryAreaPolicy deliveryAreaPolicy;
    private final DepartmentConnectionPolicy connectionPolicy;
    private final DepartmentLifecyclePolicy lifecyclePolicy;
    private final DepartmentCapacityPolicy capacityPolicy;

    public DepartmentConfiguration(final boolean autoAssignDeliveryAreas,
                                   final boolean autoConnectDepartments,
                                   final boolean preventDepartmentDeletion,
                                   final boolean preventDepartmentArchiving,
                                   final DepartmentDeliveryAreaPolicy deliveryAreaPolicy,
                                   final DepartmentConnectionPolicy connectionPolicy,
                                   final DepartmentLifecyclePolicy lifecyclePolicy,
                                   final DepartmentCapacityPolicy capacityPolicy) {
        this.autoAssignDeliveryAreas = autoAssignDeliveryAreas;
        this.autoConnectDepartments = autoConnectDepartments;
        this.preventDepartmentDeletion = preventDepartmentDeletion;
        this.preventDepartmentArchiving = preventDepartmentArchiving;
        this.deliveryAreaPolicy = deliveryAreaPolicy != null
                ? deliveryAreaPolicy : DepartmentDeliveryAreaPolicy.defaultConfiguration();
        this.connectionPolicy = connectionPolicy != null
                ? connectionPolicy : DepartmentConnectionPolicy.defaultConfiguration();
        this.lifecyclePolicy = lifecyclePolicy != null
                ? lifecyclePolicy : DepartmentLifecyclePolicy.defaultConfiguration();
        this.capacityPolicy = capacityPolicy != null
                ? capacityPolicy : DepartmentCapacityPolicy.defaultConfiguration();
    }

    public static DepartmentConfiguration defaultConfiguration() {
        return new DepartmentConfiguration(false, false, false, false, null, null, null, null);
    }

    public boolean isAutoAssignDeliveryAreas() { return autoAssignDeliveryAreas; }
    public boolean isAutoConnectDepartments() { return autoConnectDepartments; }
    public boolean isPreventDepartmentDeletion() { return preventDepartmentDeletion; }
    public boolean isPreventDepartmentArchiving() { return preventDepartmentArchiving; }
    public DepartmentDeliveryAreaPolicy getDeliveryAreaPolicy() { return deliveryAreaPolicy; }
    public DepartmentConnectionPolicy getConnectionPolicy() { return connectionPolicy; }
    public DepartmentLifecyclePolicy getLifecyclePolicy() { return lifecyclePolicy; }
    public DepartmentCapacityPolicy getCapacityPolicy() { return capacityPolicy; }
}
