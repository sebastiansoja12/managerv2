package com.warehouse.commonassets.repository;

import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseRepositoryTest {

    @Test
    void shouldAllowCriteriaLookupBeforeAuthentication() {
        final EntityManager entityManager = mock(EntityManager.class);
        final CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        @SuppressWarnings("unchecked")
        final CriteriaQuery<TestEntity> criteriaQuery = mock(CriteriaQuery.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(TestEntity.class)).thenReturn(criteriaQuery);
        final OperatorContextProvider contextProvider = Optional::empty;
        final BaseRepository<TestEntity> repository = new BaseRepository<>(entityManager, contextProvider);

        final Criteria<TestEntity> criteria = repository.createCriteria(TestEntity.class);

        assertSame(criteriaQuery, criteria.build());
    }

    private static final class TestEntity extends BelongsToOperator {
    }
}
