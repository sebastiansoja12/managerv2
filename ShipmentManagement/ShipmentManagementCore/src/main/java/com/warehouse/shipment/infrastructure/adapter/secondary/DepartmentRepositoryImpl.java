package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.shipment.domain.port.secondary.DepartmentRepository;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentReadRepository repository;

    public DepartmentRepositoryImpl(final DepartmentReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsAnyByCountry(final Country country) {
        return repository.existsAnyByCountry(country);
    }
}
