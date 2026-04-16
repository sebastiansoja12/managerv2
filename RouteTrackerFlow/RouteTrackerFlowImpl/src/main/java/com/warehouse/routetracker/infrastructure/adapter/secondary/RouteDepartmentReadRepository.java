package com.warehouse.routetracker.infrastructure.adapter.secondary;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteDepartmentReadRepository extends JpaRepository<DepartmentEntity, String> {

    Optional<DepartmentEntity> findByDepartmentCode(String departmentCode);
}
