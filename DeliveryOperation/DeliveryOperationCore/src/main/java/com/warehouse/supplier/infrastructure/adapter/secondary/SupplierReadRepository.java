package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierReadRepository extends JpaRepository<SupplierEntity, Long> {

    @EntityGraph(value = "SupplierEntity.full", type = EntityGraph.EntityGraphType.FETCH)
    List<SupplierEntity> findByDepartment_DepartmentCode(final String departmentCode);

    @EntityGraph(value = "SupplierEntity.full", type = EntityGraph.EntityGraphType.FETCH)
    Optional<SupplierEntity> findBySupplierCode(final String supplierCode);

    @EntityGraph(value = "SupplierEntity.full", type = EntityGraph.EntityGraphType.FETCH)
    @NotNull
    List<SupplierEntity> findAll();

}

