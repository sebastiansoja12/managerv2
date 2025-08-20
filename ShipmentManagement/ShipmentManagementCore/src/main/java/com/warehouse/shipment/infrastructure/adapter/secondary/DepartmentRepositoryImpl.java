package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.port.secondary.DepartmentRepository;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentReadRepository repository;

    public DepartmentRepositoryImpl(final DepartmentReadRepository repository) {
        this.repository = repository;
    }


    @Override
    public boolean existsAnyByCountryCode(final CountryCode countryCode) {
        return repository.existsAnyByCountryCode(countryCode);
    }
}
