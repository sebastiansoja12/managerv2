package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class PickupPointDefaultsPolicy {

    private final DefaultPickupPointType defaultType;
    private final boolean defaultDropOffEnabled;
    private final boolean defaultCollectionEnabled;
    private final PickupPointMaxShipmentSize servicePointMaxShipmentSize;
    private final PickupPointMaxShipmentSize parcelLockerMaxShipmentSize;
    private final boolean defaultAcceptsDangerousGoods;
    private final String weekdayOpeningTime;
    private final String weekdayClosingTime;
    private final boolean saturdayOpen;
    private final String saturdayOpeningTime;
    private final String saturdayClosingTime;
    private final boolean requireCoordinatesForActivation;

    public PickupPointDefaultsPolicy(final DefaultPickupPointType defaultType,
                                     final boolean defaultDropOffEnabled,
                                     final boolean defaultCollectionEnabled,
                                     final PickupPointMaxShipmentSize servicePointMaxShipmentSize,
                                     final PickupPointMaxShipmentSize parcelLockerMaxShipmentSize,
                                     final boolean defaultAcceptsDangerousGoods,
                                     final String weekdayOpeningTime,
                                     final String weekdayClosingTime,
                                     final boolean saturdayOpen,
                                     final String saturdayOpeningTime,
                                     final String saturdayClosingTime,
                                     final boolean requireCoordinatesForActivation) {
        this.defaultType = defaultType;
        this.defaultDropOffEnabled = defaultDropOffEnabled;
        this.defaultCollectionEnabled = defaultCollectionEnabled;
        this.servicePointMaxShipmentSize = servicePointMaxShipmentSize;
        this.parcelLockerMaxShipmentSize = parcelLockerMaxShipmentSize;
        this.defaultAcceptsDangerousGoods = defaultAcceptsDangerousGoods;
        this.weekdayOpeningTime = weekdayOpeningTime;
        this.weekdayClosingTime = weekdayClosingTime;
        this.saturdayOpen = saturdayOpen;
        this.saturdayOpeningTime = saturdayOpeningTime;
        this.saturdayClosingTime = saturdayClosingTime;
        this.requireCoordinatesForActivation = requireCoordinatesForActivation;
    }

    public static PickupPointDefaultsPolicy defaultConfiguration() {
        return new PickupPointDefaultsPolicy(
                DefaultPickupPointType.SERVICE_POINT,
                true,
                true,
                PickupPointMaxShipmentSize.MEDIUM,
                PickupPointMaxShipmentSize.AVERAGE,
                false,
                "08:00",
                "20:00",
                true,
                "09:00",
                "14:00",
                true
        );
    }

    public DefaultPickupPointType getDefaultType() { return defaultType; }
    public boolean isDefaultDropOffEnabled() { return defaultDropOffEnabled; }
    public boolean isDefaultCollectionEnabled() { return defaultCollectionEnabled; }
    public PickupPointMaxShipmentSize getServicePointMaxShipmentSize() { return servicePointMaxShipmentSize; }
    public PickupPointMaxShipmentSize getParcelLockerMaxShipmentSize() { return parcelLockerMaxShipmentSize; }
    public boolean isDefaultAcceptsDangerousGoods() { return defaultAcceptsDangerousGoods; }
    public String getWeekdayOpeningTime() { return weekdayOpeningTime; }
    public String getWeekdayClosingTime() { return weekdayClosingTime; }
    public boolean isSaturdayOpen() { return saturdayOpen; }
    public String getSaturdayOpeningTime() { return saturdayOpeningTime; }
    public String getSaturdayClosingTime() { return saturdayClosingTime; }
    public boolean isRequireCoordinatesForActivation() { return requireCoordinatesForActivation; }
}
