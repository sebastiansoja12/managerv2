package com.warehouse.depot.infrastructure.secondary;

import com.warehouse.depot.infrastructure.secondary.entity.DepotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepotReadRepository extends JpaRepository<DepotEntity, Long> {

    Optional<DepotEntity> findByDepotCode(String depotCode);
}
