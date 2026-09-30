package com.warehouse.commonassets.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.model.BelongsToOperator;

import jakarta.persistence.EntityManager;

@Repository
public class BaseRepository<T extends BelongsToOperator> implements OperatorFilteredRepository<T> {

    private final EntityManager entityManager;
    private final OperatorContextProvider operatorContextProvider;

    public BaseRepository(final EntityManager entityManager,
                          final OperatorContextProvider operatorContextProvider) {
        this.entityManager = entityManager;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    @Transactional
    public void create(final T object) {
        if (object.operatorId() == null) {
            final OperatorId operatorId = operatorContextProvider.currentOperatorId();
            object.assignOperator(operatorId);
        }
        this.entityManager.persist(object);
    }

    @Override
    @Transactional
    public void update(final T object) {
        if (object.operatorId() == null) {
            final OperatorId operatorId = operatorContextProvider.currentOperatorId();
            object.assignOperator(operatorId);
        }
        this.entityManager.merge(object);
    }

    @Override
    public void delete(final T object) {
        this.entityManager.remove(object);
    }

    @Override
    public Criteria<T> createCriteria(final Class<T> clazz) {
        final Criteria<T> criteria = new Criteria<>(entityManager, clazz);
        operatorContextProvider.currentContext()
                .map(OperatorDetails::operatorId)
                .ifPresent(operatorId -> criteria.eq("operatorId.value", operatorId.getValue()));
        return criteria;
    }
}
