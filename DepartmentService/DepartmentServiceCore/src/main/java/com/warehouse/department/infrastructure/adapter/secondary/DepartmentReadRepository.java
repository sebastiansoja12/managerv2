package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("department.departmentReadRepository")
public interface DepartmentReadRepository extends JpaRepository<DepartmentEntity, String> {

    Optional<DepartmentEntity> findByDepartmentCode(final String departmentCode);
}
