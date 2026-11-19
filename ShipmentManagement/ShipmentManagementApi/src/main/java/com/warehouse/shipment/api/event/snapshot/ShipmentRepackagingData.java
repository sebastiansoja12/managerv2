package com.warehouse.shipment.api.event.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentRepackagingData(String packagingType,
                                      BigDecimal weightInKilograms,
                                      BigDecimal lengthInCentimeters,
                                      BigDecimal widthInCentimeters,
                                      BigDecimal heightInCentimeters) {
}
