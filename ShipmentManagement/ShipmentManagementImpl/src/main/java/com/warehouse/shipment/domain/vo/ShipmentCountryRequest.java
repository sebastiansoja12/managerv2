package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentCountryRequest(ShipmentId shipmentId, CountryCode issuerCountry, CountryCode receiverCountry) {
}
