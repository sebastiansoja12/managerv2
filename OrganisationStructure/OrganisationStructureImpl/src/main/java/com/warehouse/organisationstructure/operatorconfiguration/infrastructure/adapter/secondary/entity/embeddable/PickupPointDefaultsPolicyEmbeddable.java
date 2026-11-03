package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DefaultPickupPointType;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointDefaultsPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointMaxShipmentSize;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class PickupPointDefaultsPolicyEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_point_default_type")
    private DefaultPickupPointType defaultType;

    @Column(name = "pickup_point_default_drop_off_enabled")
    private boolean defaultDropOffEnabled;

    @Column(name = "pickup_point_default_collection_enabled")
    private boolean defaultCollectionEnabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_point_service_point_max_shipment_size")
    private PickupPointMaxShipmentSize servicePointMaxShipmentSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_point_parcel_locker_max_shipment_size")
    private PickupPointMaxShipmentSize parcelLockerMaxShipmentSize;

    @Column(name = "pickup_point_default_accepts_dangerous_goods")
    private boolean defaultAcceptsDangerousGoods;

    @Column(name = "pickup_point_weekday_opening_time")
    private String weekdayOpeningTime;

    @Column(name = "pickup_point_weekday_closing_time")
    private String weekdayClosingTime;

    @Column(name = "pickup_point_saturday_open")
    private boolean saturdayOpen;

    @Column(name = "pickup_point_saturday_opening_time")
    private String saturdayOpeningTime;

    @Column(name = "pickup_point_saturday_closing_time")
    private String saturdayClosingTime;

    @Column(name = "pickup_point_require_coordinates_for_activation")
    private boolean requireCoordinatesForActivation;

    public PickupPointDefaultsPolicyEmbeddable() {
    }

    public static PickupPointDefaultsPolicyEmbeddable from(final PickupPointDefaultsPolicy configuration) {
        final PickupPointDefaultsPolicy source = configuration != null
                ? configuration
                : PickupPointDefaultsPolicy.defaultConfiguration();
        final PickupPointDefaultsPolicyEmbeddable embeddable = new PickupPointDefaultsPolicyEmbeddable();
        embeddable.defaultType = source.getDefaultType();
        embeddable.defaultDropOffEnabled = source.isDefaultDropOffEnabled();
        embeddable.defaultCollectionEnabled = source.isDefaultCollectionEnabled();
        embeddable.servicePointMaxShipmentSize = source.getServicePointMaxShipmentSize();
        embeddable.parcelLockerMaxShipmentSize = source.getParcelLockerMaxShipmentSize();
        embeddable.defaultAcceptsDangerousGoods = source.isDefaultAcceptsDangerousGoods();
        embeddable.weekdayOpeningTime = source.getWeekdayOpeningTime();
        embeddable.weekdayClosingTime = source.getWeekdayClosingTime();
        embeddable.saturdayOpen = source.isSaturdayOpen();
        embeddable.saturdayOpeningTime = source.getSaturdayOpeningTime();
        embeddable.saturdayClosingTime = source.getSaturdayClosingTime();
        embeddable.requireCoordinatesForActivation = source.isRequireCoordinatesForActivation();
        return embeddable;
    }

    public PickupPointDefaultsPolicy toModel() {
        return new PickupPointDefaultsPolicy(
                defaultType,
                defaultDropOffEnabled,
                defaultCollectionEnabled,
                servicePointMaxShipmentSize,
                parcelLockerMaxShipmentSize,
                defaultAcceptsDangerousGoods,
                weekdayOpeningTime,
                weekdayClosingTime,
                saturdayOpen,
                saturdayOpeningTime,
                saturdayClosingTime,
                requireCoordinatesForActivation
        );
    }
}
