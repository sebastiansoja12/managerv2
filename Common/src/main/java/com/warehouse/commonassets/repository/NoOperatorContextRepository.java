package com.warehouse.commonassets.repository;

public interface NoOperatorContextRepository<T> {

    Criteria<T> createCriteria(final Class<T> clazz);
}
