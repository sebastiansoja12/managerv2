package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DepartmentEntity;

public interface DepartmentReadRepository extends JpaRepository<DepartmentEntity, String> {

    @Query("SELECT d FROM device.DepartmentEntity d WHERE d.departmentCode = :departmentCode")
    Optional<DepartmentEntity> findByDepartmentCode(final String departmentCode);

}
