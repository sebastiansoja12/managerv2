package com.warehouse.supplierconfiguration.domain.enumeration;

public enum SupplierSetting {
    COPY_DEPARTMENT_CODE_FROM_DEVICE(SupplierSettingGroup.SHIPMENTS, SupplierSettingCategory.PREFERENCE, false),
    SKIP_INVALID_SHIPMENTS(SupplierSettingGroup.SHIPMENTS, SupplierSettingCategory.PREFERENCE, false),
    AUTO_GENERATE_LABELS(SupplierSettingGroup.SHIPMENTS, SupplierSettingCategory.PREFERENCE, false),
    REQUIRE_PICKUP_SCAN(SupplierSettingGroup.SHIPMENTS, SupplierSettingCategory.MANDATORY_REQUIREMENT, true),
    REQUIRE_DELIVERY_SCAN(SupplierSettingGroup.SHIPMENTS, SupplierSettingCategory.MANDATORY_REQUIREMENT, true),
    ALLOW_MANUAL_SHIPMENT_ADDITION(SupplierSettingGroup.SHIPMENTS, SupplierSettingCategory.OPERATIONAL_PERMISSION, false),
    ALLOW_SHIPMENT_TRANSFER(SupplierSettingGroup.SHIPMENTS, SupplierSettingCategory.OPERATIONAL_PERMISSION, false),
    ALLOW_SHIPMENT_UNASSIGNMENT(SupplierSettingGroup.SHIPMENTS, SupplierSettingCategory.OPERATIONAL_PERMISSION, false),

    REQUIRE_PROOF_OF_DELIVERY(SupplierSettingGroup.DELIVERIES, SupplierSettingCategory.MANDATORY_REQUIREMENT, true),
    REQUIRE_RECIPIENT_SIGNATURE(SupplierSettingGroup.DELIVERIES, SupplierSettingCategory.MANDATORY_REQUIREMENT, false),
    REQUIRE_DELIVERY_PHOTO(SupplierSettingGroup.DELIVERIES, SupplierSettingCategory.MANDATORY_REQUIREMENT, false),
    REQUIRE_NON_DELIVERY_REASON(SupplierSettingGroup.DELIVERIES, SupplierSettingCategory.MANDATORY_REQUIREMENT, true),
    ALLOW_DELIVERY_ADDRESS_CHANGE(SupplierSettingGroup.DELIVERIES, SupplierSettingCategory.OPERATIONAL_PERMISSION, false),

    ALLOW_DELIVERY_AREA_EDITING(SupplierSettingGroup.ROUTES, SupplierSettingCategory.OPERATIONAL_PERMISSION, false),
    ALLOW_OUT_OF_AREA_DELIVERY(SupplierSettingGroup.ROUTES, SupplierSettingCategory.OPERATIONAL_PERMISSION, false),
    ALLOW_DELIVERY_REORDERING(SupplierSettingGroup.ROUTES, SupplierSettingCategory.OPERATIONAL_PERMISSION, true),
    AUTO_ASSIGN_SHIPMENTS_BY_AREA(SupplierSettingGroup.ROUTES, SupplierSettingCategory.PREFERENCE, false),

    GENERATE_NEW_RETURN_CODES(SupplierSettingGroup.RETURNS, SupplierSettingCategory.PREFERENCE, false),
    AUTO_MARK_FOR_RETURN(SupplierSettingGroup.RETURNS, SupplierSettingCategory.PREFERENCE, false),
    REQUIRE_RETURN_SCAN(SupplierSettingGroup.RETURNS, SupplierSettingCategory.MANDATORY_REQUIREMENT, true),
    AUTO_GENERATE_RETURN_LABELS(SupplierSettingGroup.RETURNS, SupplierSettingCategory.PREFERENCE, false),

    NOTIFY_ABOUT_NEW_SHIPMENTS(SupplierSettingGroup.PREFERENCES, SupplierSettingCategory.PREFERENCE, true),
    NOTIFY_ABOUT_ROUTE_CHANGES(SupplierSettingGroup.PREFERENCES, SupplierSettingCategory.PREFERENCE, true),
    CONFIRM_SHIPMENT_TRANSFER(SupplierSettingGroup.PREFERENCES, SupplierSettingCategory.PREFERENCE, true);

    private final SupplierSettingGroup group;
    private final SupplierSettingCategory category;
    private final boolean defaultValue;

    SupplierSetting(final SupplierSettingGroup group,
                    final SupplierSettingCategory category,
                    final boolean defaultValue) {
        this.group = group;
        this.category = category;
        this.defaultValue = defaultValue;
    }

    public SupplierSettingGroup getGroup() {
        return group;
    }

    public SupplierSettingCategory getCategory() {
        return category;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }
}
