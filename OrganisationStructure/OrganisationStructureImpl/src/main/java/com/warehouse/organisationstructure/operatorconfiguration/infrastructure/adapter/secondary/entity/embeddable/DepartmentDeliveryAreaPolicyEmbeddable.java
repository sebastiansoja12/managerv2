package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DeliveryAreaMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentDeliveryAreaPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.UncoveredPostalCodePolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class DepartmentDeliveryAreaPolicyEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "department_delivery_area_mode")
    private DeliveryAreaMode deliveryAreaMode;

    @Column(name = "department_max_delivery_radius_km")
    private int maxDeliveryRadiusKm;

    @Column(name = "department_allow_manual_delivery_area_edit")
    private boolean allowManualDeliveryAreaEdit;

    @Column(name = "department_recalculate_areas_on_status_change")
    private boolean recalculateAreasOnStatusChange;

    @Enumerated(EnumType.STRING)
    @Column(name = "department_uncovered_postal_code_policy")
    private UncoveredPostalCodePolicy uncoveredPostalCodePolicy;

    public DepartmentDeliveryAreaPolicyEmbeddable() {
    }

    public static DepartmentDeliveryAreaPolicyEmbeddable from(final DepartmentDeliveryAreaPolicy configuration) {
        final DepartmentDeliveryAreaPolicy source = configuration != null
                ? configuration
                : DepartmentDeliveryAreaPolicy.defaultConfiguration();
        final DepartmentDeliveryAreaPolicyEmbeddable embeddable = new DepartmentDeliveryAreaPolicyEmbeddable();
        embeddable.deliveryAreaMode = source.getDeliveryAreaMode();
        embeddable.maxDeliveryRadiusKm = source.getMaxDeliveryRadiusKm();
        embeddable.allowManualDeliveryAreaEdit = source.isAllowManualDeliveryAreaEdit();
        embeddable.recalculateAreasOnStatusChange = source.isRecalculateAreasOnStatusChange();
        embeddable.uncoveredPostalCodePolicy = source.getUncoveredPostalCodePolicy();
        return embeddable;
    }

    public DepartmentDeliveryAreaPolicy toModel() {
        return new DepartmentDeliveryAreaPolicy(
                deliveryAreaMode,
                maxDeliveryRadiusKm,
                allowManualDeliveryAreaEdit,
                recalculateAreasOnStatusChange,
                uncoveredPostalCodePolicy
        );
    }
}
