package com.warehouse.delivery.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DepotEntity;

@Repository("delivery.departmentReadRepository")
public interface DepartmentReadRepository extends JpaRepository<DepotEntity, String> {
    Optional<DepotEntity> findByDepartmentCode(final String depotCode);
}
