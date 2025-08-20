package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.port.secondary.CountryRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.CountryEntity;

public class CountryRepositoryImpl implements CountryRepository {

    private final CountryReadRepository repository;

    public CountryRepositoryImpl(final CountryReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Country getCountryNameByCode(final CountryCode countryCode) {
        return repository.findById(countryCode)
                .map(CountryEntity::getName)
                .map(Country::valueOf)
                .orElse(Country.UNDEFINED);
    }
}
