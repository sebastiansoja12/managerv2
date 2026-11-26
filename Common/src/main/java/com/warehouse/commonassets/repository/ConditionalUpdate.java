package com.warehouse.commonassets.repository;

import com.warehouse.commonassets.identificator.ObjectValue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConditionalUpdate<T> {

    private final EntityManager em;
    private final CriteriaBuilder cb;
    private final CriteriaUpdate<T> update;
    private final Root<T> root;
    private final List<Predicate> predicates = new ArrayList<>();

    public ConditionalUpdate(final EntityManager em, final Class<T> clazz) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
        this.update = cb.createCriteriaUpdate(clazz);
        this.root = update.from(clazz);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ConditionalUpdate<T> set(final String fieldName, final Object value) {
        update.set((Path) path(fieldName), unwrap(value));
        return this;
    }

    @SuppressWarnings("unchecked")
    public ConditionalUpdate<T> increment(final String fieldName) {
        final Path<Long> path = (Path<Long>) path(fieldName);
        final Expression<Long> incremented = cb.sum(path, 1L);
        update.set(path, incremented);
        return this;
    }

    public ConditionalUpdate<T> eq(final String fieldName, final Object value) {
        return and(cb.equal(path(fieldName), unwrap(value)));
    }

    public ConditionalUpdate<T> isNull(final String fieldName) {
        return and(cb.isNull(path(fieldName)));
    }

    public ConditionalUpdate<T> in(final String fieldName, final Collection<?> values) {
        return and(path(fieldName).in(values));
    }

    public ConditionalUpdate<T> and(final Predicate predicate) {
        predicates.add(predicate);
        return this;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return cb;
    }

    public Root<T> getRoot() {
        return root;
    }

    public int execute() {
        update.where(predicates.toArray(Predicate[]::new));
        return em.createQuery(update).executeUpdate();
    }

    private Path<?> path(final String fieldName) {
        Path<?> path = root;
        for (final String part : fieldName.split("\\.")) {
            path = path.get(part);
        }
        return path;
    }

    private Object unwrap(final Object value) {
        if (value instanceof ObjectValue<?> objectValue) {
            return objectValue.value();
        }
        return value;
    }
}
