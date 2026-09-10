package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShippingCapabilities;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ShippingCapabilitiesEmbeddable {

    @Column(name = "supports_domestic_shipping")
    private boolean supportsDomesticShipping;

    @Column(name = "supports_international_shipping")
    private boolean supportsInternationalShipping;

    @Column(name = "supports_express_shipping")
    private boolean supportsExpressShipping;

    @Column(name = "supports_same_day_delivery")
    private boolean supportsSameDayDelivery;

    @Column(name = "supports_cash_on_delivery")
    private boolean supportsCashOnDelivery;

    @Column(name = "supports_parcel_lockers")
    private boolean supportsParcelLockers;

    @Column(name = "supports_pickup_points")
    private boolean supportsPickupPoints;

    @Column(name = "supports_home_delivery")
    private boolean supportsHomeDelivery;

    @Column(name = "supports_saturday_delivery")
    private boolean supportsSaturdayDelivery;

    @Column(name = "supports_sunday_delivery")
    private boolean supportsSundayDelivery;

    @Column(name = "supports_return_shipments")
    private boolean supportsReturnShipments;

    @Column(name = "provides_tracking")
    private boolean providesTracking;

    @Column(name = "provides_insurance")
    private boolean providesInsurance;

    public ShippingCapabilitiesEmbeddable() {
    }

    public static ShippingCapabilitiesEmbeddable from(
            final ShippingCapabilities capabilities) {
        final ShippingCapabilitiesEmbeddable embeddable = new ShippingCapabilitiesEmbeddable();
        if (capabilities != null) {
            embeddable.supportsDomesticShipping = capabilities.isSupportsDomesticShipping();
            embeddable.supportsInternationalShipping = capabilities.isSupportsInternationalShipping();
            embeddable.supportsExpressShipping = capabilities.isSupportsExpressShipping();
            embeddable.supportsSameDayDelivery = capabilities.isSupportsSameDayDelivery();
            embeddable.supportsCashOnDelivery = capabilities.isSupportsCashOnDelivery();
            embeddable.supportsParcelLockers = capabilities.isSupportsParcelLockers();
            embeddable.supportsPickupPoints = capabilities.isSupportsPickupPoints();
            embeddable.supportsHomeDelivery = capabilities.isSupportsHomeDelivery();
            embeddable.supportsSaturdayDelivery = capabilities.isSupportsSaturdayDelivery();
            embeddable.supportsSundayDelivery = capabilities.isSupportsSundayDelivery();
            embeddable.supportsReturnShipments = capabilities.isSupportsReturnShipments();
            embeddable.providesTracking = capabilities.isProvidesTracking();
            embeddable.providesInsurance = capabilities.isProvidesInsurance();
        }
        return embeddable;
    }

    public ShippingCapabilities toModel() {
        return new ShippingCapabilities(
                supportsDomesticShipping,
                supportsInternationalShipping,
                supportsExpressShipping,
                supportsSameDayDelivery,
                supportsCashOnDelivery,
                supportsParcelLockers,
                supportsPickupPoints,
                supportsHomeDelivery,
                supportsSaturdayDelivery,
                supportsSundayDelivery,
                supportsReturnShipments,
                providesTracking,
                providesInsurance
        );
    }
}
