package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShippingCapabilities {
    private boolean supportsDomesticShipping;
    private boolean supportsInternationalShipping;
    private boolean supportsExpressShipping;
    private boolean supportsSameDayDelivery;
    private boolean supportsCashOnDelivery;
    private boolean supportsParcelLockers;
    private boolean supportsPickupPoints;
    private boolean supportsHomeDelivery;
    private boolean supportsSaturdayDelivery;
    private boolean supportsSundayDelivery;
    private boolean supportsReturnShipments;
    private boolean providesTracking;
    private boolean providesInsurance;

    public ShippingCapabilities() {
    }

    public ShippingCapabilities(final boolean supportsDomesticShipping,
                                final boolean supportsInternationalShipping,
                                final boolean supportsExpressShipping,
                                final boolean supportsSameDayDelivery,
                                final boolean supportsCashOnDelivery,
                                final boolean supportsParcelLockers,
                                final boolean supportsPickupPoints,
                                final boolean supportsHomeDelivery,
                                final boolean supportsSaturdayDelivery,
                                final boolean supportsSundayDelivery,
                                final boolean supportsReturnShipments,
                                final boolean providesTracking,
                                final boolean providesInsurance) {
        this.supportsDomesticShipping = supportsDomesticShipping;
        this.supportsInternationalShipping = supportsInternationalShipping;
        this.supportsExpressShipping = supportsExpressShipping;
        this.supportsSameDayDelivery = supportsSameDayDelivery;
        this.supportsCashOnDelivery = supportsCashOnDelivery;
        this.supportsParcelLockers = supportsParcelLockers;
        this.supportsPickupPoints = supportsPickupPoints;
        this.supportsHomeDelivery = supportsHomeDelivery;
        this.supportsSaturdayDelivery = supportsSaturdayDelivery;
        this.supportsSundayDelivery = supportsSundayDelivery;
        this.supportsReturnShipments = supportsReturnShipments;
        this.providesTracking = providesTracking;
        this.providesInsurance = providesInsurance;
    }

    public boolean isSupportsDomesticShipping() { return supportsDomesticShipping; }
    public boolean isSupportsInternationalShipping() { return supportsInternationalShipping; }
    public boolean isSupportsExpressShipping() { return supportsExpressShipping; }
    public boolean isSupportsSameDayDelivery() { return supportsSameDayDelivery; }
    public boolean isSupportsCashOnDelivery() { return supportsCashOnDelivery; }
    public boolean isSupportsParcelLockers() { return supportsParcelLockers; }
    public boolean isSupportsPickupPoints() { return supportsPickupPoints; }
    public boolean isSupportsHomeDelivery() { return supportsHomeDelivery; }
    public boolean isSupportsSaturdayDelivery() { return supportsSaturdayDelivery; }
    public boolean isSupportsSundayDelivery() { return supportsSundayDelivery; }
    public boolean isSupportsReturnShipments() { return supportsReturnShipments; }
    public boolean isProvidesTracking() { return providesTracking; }
    public boolean isProvidesInsurance() { return providesInsurance; }
}
