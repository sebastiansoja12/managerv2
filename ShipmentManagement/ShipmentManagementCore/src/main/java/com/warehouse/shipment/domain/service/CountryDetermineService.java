package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.exception.enumeration.ShipmentErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.CountryDetermine;
import com.warehouse.shipment.domain.model.ShipmentCreateRequest;

public interface CountryDetermineService {
    Result<CountryDetermine, ShipmentErrorCode> determineCountry(final ShipmentCreateRequest request);
}
