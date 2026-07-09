package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentCountry(Country originCountry, Country destinationCountry, ShipmentId shipmentId) {
}
