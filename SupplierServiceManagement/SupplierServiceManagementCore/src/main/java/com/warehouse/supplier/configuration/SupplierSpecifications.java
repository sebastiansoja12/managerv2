package com.warehouse.supplier.configuration;

import org.springframework.data.jpa.domain.Specification;

import com.warehouse.supplier.infrastructure.adapter.secondary.entity.DepartmentCode;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;

public class SupplierSpecifications {

    public static Specification<SupplierEntity> departmentEquals(final DepartmentCode departmentCode) {
        return (root, query, cb) ->
                cb.equal(root.get("departmentCode"), departmentCode);
    }

    public static <T> Specification<T> alwaysTrue() {
        return (root, query, cb) -> cb.conjunction();
    }
}

