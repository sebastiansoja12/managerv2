package com.warehouse.commonassets.repository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.model.BelongsToDepartment;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;

import jakarta.persistence.EntityManager;

@Repository
public class BaseRepository<T extends BelongsToDepartment> implements TenantFilteredRepository<T> {

    private final EntityManager entityManager;

    public BaseRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void create(final T object) {
        final DepartmentCode departmentCode = this.getDepartmentCode();
        object.assignDepartment(departmentCode);
        this.entityManager.persist(object);
    }

    @Override
    public void update(final T object) {
        this.entityManager.merge(object);
    }

    @Override
    public void delete(final T object) {
        this.entityManager.remove(object);
    }

    @Override
    public Criteria<T> createCriteria(final Class<T> clazz) {
        final Criteria<T> criteria = new Criteria<>(entityManager, clazz);
		criteria.and(
				criteria.getCriteriaBuilder().equal(criteria.getRoot().get("departmentCode"), getDepartmentCode()));
        return criteria;
    }

    private DepartmentCode getDepartmentCode() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernameTenantPasswordAuthenticationToken token) {
            return token.getDepartmentCode();
        } else {
            throw new IllegalStateException("No department code found");
        }
    }
}
