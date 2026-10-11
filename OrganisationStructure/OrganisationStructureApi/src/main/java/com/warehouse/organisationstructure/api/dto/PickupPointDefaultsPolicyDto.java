package com.warehouse.organisationstructure.api.dto;

public record PickupPointDefaultsPolicyDto(
        DefaultPickupPointTypeDto defaultType,
        boolean defaultDropOffEnabled,
        boolean defaultCollectionEnabled,
        PickupPointMaxShipmentSizeDto servicePointMaxShipmentSize,
        PickupPointMaxShipmentSizeDto parcelLockerMaxShipmentSize,
        boolean defaultAcceptsDangerousGoods,
        String weekdayOpeningTime,
        String weekdayClosingTime,
        boolean saturdayOpen,
        String saturdayOpeningTime,
        String saturdayClosingTime,
        boolean requireCoordinatesForActivation
) {
}
