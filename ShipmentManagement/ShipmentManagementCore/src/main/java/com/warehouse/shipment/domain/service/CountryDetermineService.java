package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.CountryDetermine;
import com.warehouse.shipment.domain.model.ShipmentCreateCommand;

public interface CountryDetermineService {
    Result<CountryDetermine, ErrorCode> determineCountry(final ShipmentCreateCommand request);

    Country determineCountryByCode(final CountryCode issuerCountryCode);
}
