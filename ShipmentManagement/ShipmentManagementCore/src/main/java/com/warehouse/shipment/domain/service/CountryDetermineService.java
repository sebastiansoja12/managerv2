package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.model.Shipment;

public interface CountryDetermineService {
    void determineCountry(final Shipment shipment);
}
