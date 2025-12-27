package com.warehouse.commonassets.repository;

import com.warehouse.commonassets.identificator.ObjectValue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;

import java.util.Optional;

public class Criteria<T> {
    private final EntityManager em;
    private final CriteriaBuilder cb;
    private final CriteriaQuery<T> cq;
    private final Root<T> root;

    public Criteria(final EntityManager em, Class<T> clazz) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
        this.cq = cb.createQuery(clazz);
        this.root = cq.from(clazz);
    }

    public Criteria<T> and(final String fieldName, final ObjectValue value) {
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

        final Predicate predicate = cb.equal(path, value.value());

        final Predicate restriction = cq.getRestriction();
        if (restriction == null) {
            cq.where(predicate);
        } else {
            cq.where(cb.and(restriction, predicate));
        }

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

    public Optional<T> getSingleResult() {
        try {
            return Optional.of(em.createQuery(cq).getSingleResult());
        } catch (final NoResultException e) {
            return Optional.empty();
        }
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
}

