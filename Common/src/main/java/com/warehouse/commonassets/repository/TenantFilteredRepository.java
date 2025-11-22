package com.warehouse.commonassets.repository;

public interface TenantFilteredRepository<T> {

    void create(final T object);
    Criteria<T> createCriteria(final Class<T> clazz);

}

