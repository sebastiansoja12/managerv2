package com.warehouse.commonassets.repository;

import com.warehouse.commonassets.identificator.ObjectValue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Criteria<T> {
    private final EntityManager em;
    private final CriteriaBuilder cb;
    private final CriteriaQuery<T> cq;
    private final Root<T> root;
    private Integer firstResult;
    private Integer maxResults;

    public Criteria(final EntityManager em, Class<T> clazz) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
        this.cq = cb.createQuery(clazz);
        this.root = cq.from(clazz);
    }

    public Criteria<T> and(final String fieldName, final ObjectValue value) {
        return eq(fieldName, value);
    }

    public Criteria<T> eq(final String fieldName, final Object value) {
        return and(cb.equal(path(fieldName), unwrap(value)));
    }

    public Criteria<T> ne(final String fieldName, final Object value) {
        return and(cb.notEqual(path(fieldName), unwrap(value)));
    }

    public Criteria<T> isTrue(final String fieldName) {
        return and(cb.isTrue(path(fieldName).as(Boolean.class)));
    }

    public Criteria<T> isFalse(final String fieldName) {
        return and(cb.isFalse(path(fieldName).as(Boolean.class)));
    }

    public Criteria<T> isNull(final String fieldName) {
        return and(cb.isNull(path(fieldName)));
    }

    public Criteria<T> isNotNull(final String fieldName) {
        return and(cb.isNotNull(path(fieldName)));
    }

    public Criteria<T> like(final String fieldName, final String value) {
        return and(cb.like(path(fieldName).as(String.class), value));
    }

    public Criteria<T> notLike(final String fieldName, final String value) {
        return and(cb.notLike(path(fieldName).as(String.class), value));
    }

    public Criteria<T> ilike(final String fieldName, final String value) {
        return and(cb.like(cb.lower(path(fieldName).as(String.class)), value.toLowerCase(Locale.ROOT)));
    }

    public Criteria<T> contains(final String fieldName, final String value) {
        return like(fieldName, "%" + value + "%");
    }

    public Criteria<T> startsWith(final String fieldName, final String value) {
        return like(fieldName, value + "%");
    }

    public Criteria<T> endsWith(final String fieldName, final String value) {
        return like(fieldName, "%" + value);
    }

    public Criteria<T> in(final String fieldName, final Collection<?> values) {
        return and(path(fieldName).in(values.stream().map(this::unwrap).toList()));
    }

    public Criteria<T> notIn(final String fieldName, final Collection<?> values) {
        return and(cb.not(path(fieldName).in(values.stream().map(this::unwrap).toList())));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Criteria<T> gt(final String fieldName, final Comparable value) {
        return and(cb.greaterThan((Expression) path(fieldName), value));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Criteria<T> ge(final String fieldName, final Comparable value) {
        return and(cb.greaterThanOrEqualTo((Expression) path(fieldName), value));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Criteria<T> lt(final String fieldName, final Comparable value) {
        return and(cb.lessThan((Expression) path(fieldName), value));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Criteria<T> le(final String fieldName, final Comparable value) {
        return and(cb.lessThanOrEqualTo((Expression) path(fieldName), value));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Criteria<T> between(final String fieldName, final Comparable from, final Comparable to) {
        return and(cb.between((Expression) path(fieldName), from, to));
    }

    public Criteria<T> asc(final String fieldName) {
        cq.orderBy(cb.asc(path(fieldName)));
        return this;
    }

    public Criteria<T> desc(final String fieldName) {
        cq.orderBy(cb.desc(path(fieldName)));
        return this;
    }

    public Criteria<T> firstResult(final int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    public Criteria<T> maxResults(final int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public Criteria<T> and(final Predicate additionalPredicate) {
        final Predicate current = cq.getRestriction();

        if (current != null && additionalPredicate != null) {
            cq.where(cb.and(current, additionalPredicate));
        } else if (current != null) {
            cq.where(current);
        } else if (additionalPredicate != null) {
            cq.where(additionalPredicate);
        }
        return this;
    }

    public Optional<T> one() {
        try {
            return Optional.of(query().getSingleResult());
        } catch (final NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<T> getSingleResult() {
        return one();
    }

    public List<T> list() {
        return query().getResultList();
    }

    public List<T> getResultList() {
        return list();
    }

    public CriteriaQuery<T> build() {
        return cq;
    }

    public Root<T> getRoot() {
        return root;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return cb;
    }

    private Path<?> path(final String fieldName) {
        Path<?> path;
        if (fieldName.contains(".")) {
            final String[] parts = fieldName.split("\\.");
            path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
        } else {
            path = root.get(fieldName);
        }
        return path;
    }

    private Object unwrap(final Object value) {
        if (value instanceof ObjectValue objectValue) {
            return objectValue.value();
        }
        return value;
    }

    private TypedQuery<T> query() {
        final TypedQuery<T> query = em.createQuery(cq);
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }
        return query;
    }
}
