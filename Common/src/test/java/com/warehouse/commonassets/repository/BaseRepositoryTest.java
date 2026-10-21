package com.warehouse.commonassets.repository;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BaseRepositoryTest {

    private static final OperatorId OPERATOR_ID = OperatorId.of(20L);

    private EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;

    private CriteriaQuery<TestEntity> criteriaQuery;

    private Path<Object> operatorIdValuePath;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        entityManager = mock(EntityManager.class);
        criteriaBuilder = mock(CriteriaBuilder.class);
        criteriaQuery = mock(CriteriaQuery.class);
        final Root<TestEntity> root = mock(Root.class);
        final Path<Object> operatorIdPath = mock(Path.class);
        operatorIdValuePath = mock(Path.class);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(TestEntity.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(TestEntity.class)).thenReturn(root);
        when(root.get("operatorId")).thenReturn(operatorIdPath);
        when(operatorIdPath.get("value")).thenReturn(operatorIdValuePath);
    }

    @Test
    void shouldRequireOperatorContextForCriteriaLookup() {
        final OperatorContextProvider contextProvider = Optional::empty;
        final BaseRepository<TestEntity> repository = new BaseRepository<>(entityManager, contextProvider);

        assertThrows(IllegalStateException.class, () -> repository.createCriteria(TestEntity.class));
    }

    @Test
    void shouldFilterCriteriaByCurrentOperator() {
        final Predicate operatorPredicate = mock(Predicate.class);
        when(criteriaBuilder.equal(any(Path.class), any(Object.class))).thenReturn(operatorPredicate);
        final OperatorContextProvider contextProvider = () -> Optional.of(new OperatorDetails(OPERATOR_ID, null, null));
        final BaseRepository<TestEntity> repository = new BaseRepository<>(entityManager, contextProvider);

        final Criteria<TestEntity> criteria = repository.createCriteria(TestEntity.class);

        assertSame(criteriaQuery, criteria.build());
        verify(criteriaBuilder).equal(operatorIdValuePath, OPERATOR_ID.value());
        verify(criteriaQuery).where(operatorPredicate);
    }

    private static final class TestEntity extends BelongsToOperator {
    }
}
