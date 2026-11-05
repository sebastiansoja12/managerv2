package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class DepartmentDeliveryAreaPolicy {

    private final DeliveryAreaMode deliveryAreaMode;
    private final int maxDeliveryRadiusKm;
    private final boolean allowManualDeliveryAreaEdit;
    private final boolean recalculateAreasOnStatusChange;
    private final UncoveredPostalCodePolicy uncoveredPostalCodePolicy;

    public DepartmentDeliveryAreaPolicy(final DeliveryAreaMode deliveryAreaMode,
                                        final int maxDeliveryRadiusKm,
                                        final boolean allowManualDeliveryAreaEdit,
                                        final boolean recalculateAreasOnStatusChange,
                                        final UncoveredPostalCodePolicy uncoveredPostalCodePolicy) {
        this.deliveryAreaMode = deliveryAreaMode;
        this.maxDeliveryRadiusKm = maxDeliveryRadiusKm;
        this.allowManualDeliveryAreaEdit = allowManualDeliveryAreaEdit;
        this.recalculateAreasOnStatusChange = recalculateAreasOnStatusChange;
        this.uncoveredPostalCodePolicy = uncoveredPostalCodePolicy;
    }

    public static DepartmentDeliveryAreaPolicy defaultConfiguration() {
        return new DepartmentDeliveryAreaPolicy(
                DeliveryAreaMode.COUNTRY_PARTITION,
                50,
                true,
                false,
                UncoveredPostalCodePolicy.WARN
        );
    }

    public DeliveryAreaMode getDeliveryAreaMode() { return deliveryAreaMode; }
    public int getMaxDeliveryRadiusKm() { return maxDeliveryRadiusKm; }
    public boolean isAllowManualDeliveryAreaEdit() { return allowManualDeliveryAreaEdit; }
    public boolean isRecalculateAreasOnStatusChange() { return recalculateAreasOnStatusChange; }
    public UncoveredPostalCodePolicy getUncoveredPostalCodePolicy() { return uncoveredPostalCodePolicy; }
}
