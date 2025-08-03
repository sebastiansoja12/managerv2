package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.Country;

public record CountryDetermine(Country originCountry, Country destinationCountry) {}
