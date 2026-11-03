package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentSlaConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ShipmentSlaConfigurationEmbeddable {

    @Column(name = "shipment_economy_sla_hours")
    private int economySlaHours;

    @Column(name = "shipment_standard_sla_hours")
    private int standardSlaHours;

    @Column(name = "shipment_express_sla_hours")
    private int expressSlaHours;

    @Column(name = "shipment_flag_delayed_after_hours")
    private int flagDelayedAfterHours;

    @Column(name = "shipment_require_exception_note")
    private boolean requireExceptionNote;

    @Column(name = "shipment_escalate_delays_to_department_manager")
    private boolean escalateDelaysToDepartmentManager;

    public ShipmentSlaConfigurationEmbeddable() {
    }

    public static ShipmentSlaConfigurationEmbeddable from(final ShipmentSlaConfiguration configuration) {
        final ShipmentSlaConfiguration source = configuration != null
                ? configuration
                : ShipmentSlaConfiguration.defaultConfiguration();
        final ShipmentSlaConfigurationEmbeddable embeddable = new ShipmentSlaConfigurationEmbeddable();
        embeddable.economySlaHours = source.getEconomySlaHours();
        embeddable.standardSlaHours = source.getStandardSlaHours();
        embeddable.expressSlaHours = source.getExpressSlaHours();
        embeddable.flagDelayedAfterHours = source.getFlagDelayedAfterHours();
        embeddable.requireExceptionNote = source.isRequireExceptionNote();
        embeddable.escalateDelaysToDepartmentManager = source.isEscalateDelaysToDepartmentManager();
        return embeddable;
    }

    public ShipmentSlaConfiguration toModel() {
        return new ShipmentSlaConfiguration(
                economySlaHours,
                standardSlaHours,
                expressSlaHours,
                flagDelayedAfterHours,
                requireExceptionNote,
                escalateDelaysToDepartmentManager
        );
    }
}
