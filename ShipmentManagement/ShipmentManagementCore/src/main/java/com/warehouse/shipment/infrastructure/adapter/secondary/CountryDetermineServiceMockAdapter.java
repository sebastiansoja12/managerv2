package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.shipment.domain.port.secondary.CountryDetermineServicePort;
import com.warehouse.shipment.domain.vo.LocationInfo;
import com.warehouse.shipment.domain.vo.ShipmentCountry;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CountryDetermineServiceMockAdapter implements CountryDetermineServicePort {


    @Override
    public ShipmentCountry determineCountry(final LocationInfo locationInfo) {
        return null;
    }
}
