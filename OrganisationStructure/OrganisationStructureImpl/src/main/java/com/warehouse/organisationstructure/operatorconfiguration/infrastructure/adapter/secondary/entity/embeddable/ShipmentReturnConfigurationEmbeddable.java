package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentReturnConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ShipmentReturnConfigurationEmbeddable {

    @Column(name = "shipment_allow_returns")
    private boolean allowReturns;

    @Column(name = "shipment_return_window_days")
    private int returnWindowDays;

    @Column(name = "shipment_auto_return_after_failed_delivery")
    private boolean autoReturnAfterFailedDelivery;

    @Column(name = "shipment_pickup_point_storage_days")
    private int pickupPointStorageDays;

    @Column(name = "shipment_require_return_reason")
    private boolean requireReturnReason;

    public ShipmentReturnConfigurationEmbeddable() {
    }

    public static ShipmentReturnConfigurationEmbeddable from(final ShipmentReturnConfiguration configuration) {
        final ShipmentReturnConfiguration source = configuration != null
                ? configuration
                : ShipmentReturnConfiguration.defaultConfiguration();
        final ShipmentReturnConfigurationEmbeddable embeddable = new ShipmentReturnConfigurationEmbeddable();
        embeddable.allowReturns = source.isAllowReturns();
        embeddable.returnWindowDays = source.getReturnWindowDays();
        embeddable.autoReturnAfterFailedDelivery = source.isAutoReturnAfterFailedDelivery();
        embeddable.pickupPointStorageDays = source.getPickupPointStorageDays();
        embeddable.requireReturnReason = source.isRequireReturnReason();
        return embeddable;
    }

    public ShipmentReturnConfiguration toModel() {
        return new ShipmentReturnConfiguration(
                allowReturns,
                returnWindowDays,
                autoReturnAfterFailedDelivery,
                pickupPointStorageDays,
                requireReturnReason
        );
    }
}
