package com.warehouse.commonassets.repository;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

@Repository
public class NoOperatorContextBaseRepository<T> implements NoOperatorContextRepository<T> {

    private final EntityManager entityManager;

    public NoOperatorContextBaseRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Criteria<T> createCriteria(final Class<T> clazz) {
        return new Criteria<>(entityManager, clazz);
    }
}
