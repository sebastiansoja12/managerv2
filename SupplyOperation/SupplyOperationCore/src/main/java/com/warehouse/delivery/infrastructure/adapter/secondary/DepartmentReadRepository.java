package com.warehouse.delivery.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DepartmentEntity;

@Repository("delivery.departmentReadRepository")
public interface DepartmentReadRepository extends JpaRepository<DepartmentEntity, String> {
    Optional<DepartmentEntity> findByDepartmentCode(final String depotCode);
}
