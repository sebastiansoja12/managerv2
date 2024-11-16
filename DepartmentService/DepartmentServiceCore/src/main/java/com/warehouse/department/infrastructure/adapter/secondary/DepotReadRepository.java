package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepotReadRepository extends JpaRepository<DepartmentEntity, String> {

    Optional<DepartmentEntity> findByDepotCode(String depotCode);
}
