package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.CountryDetermine;
import com.warehouse.shipment.domain.vo.Party;

public interface CountryDetermineService {
    Result<CountryDetermine, ErrorCode> determineCountry(final Party sender, final Party recipient);

    Country determineCountryByCode(final CountryCode issuerCountryCode);
}
