package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.vo.LocationInfo;
import com.warehouse.shipment.domain.vo.ShipmentCountry;

public interface CountryDetermineServicePort {
    ShipmentCountry determineCountry(final LocationInfo locationInfo);
}
