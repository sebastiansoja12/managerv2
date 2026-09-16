package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class DepartmentConfiguration {

    private final boolean autoAssignDeliveryAreas;
    private final boolean autoConnectDepartments;
    private final boolean preventDepartmentDeletion;
    private final boolean preventDepartmentArchiving;

    public DepartmentConfiguration(final boolean autoAssignDeliveryAreas,
                                   final boolean autoConnectDepartments,
                                   final boolean preventDepartmentDeletion,
                                   final boolean preventDepartmentArchiving) {
        this.autoAssignDeliveryAreas = autoAssignDeliveryAreas;
        this.autoConnectDepartments = autoConnectDepartments;
        this.preventDepartmentDeletion = preventDepartmentDeletion;
        this.preventDepartmentArchiving = preventDepartmentArchiving;
    }

    public static DepartmentConfiguration defaultConfiguration() {
        return new DepartmentConfiguration(false, false, false, false);
    }

    public boolean isAutoAssignDeliveryAreas() { return autoAssignDeliveryAreas; }
    public boolean isAutoConnectDepartments() { return autoConnectDepartments; }
    public boolean isPreventDepartmentDeletion() { return preventDepartmentDeletion; }
    public boolean isPreventDepartmentArchiving() { return preventDepartmentArchiving; }
}
