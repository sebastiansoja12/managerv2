package com.warehouse.shipment.application.service;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.CountryDetermine;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;

public interface CountryDetermineService {
    Result<CountryDetermine, ErrorCode> determineCountry(final Sender sender, final Recipient recipient);

    Country determineCountryByCode(final CountryCode issuerCountryCode);
}
