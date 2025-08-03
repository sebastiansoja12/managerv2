package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentCountryRequest(ShipmentId shipmentId, Country originCountry, Country destinationCountry) {
}
