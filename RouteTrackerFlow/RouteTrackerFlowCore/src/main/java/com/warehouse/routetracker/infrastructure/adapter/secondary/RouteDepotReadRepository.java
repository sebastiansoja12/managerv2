package com.warehouse.routetracker.infrastructure.adapter.secondary;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.DepotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteDepotReadRepository extends JpaRepository<DepotEntity, String> {

    Optional<DepotEntity> findByDepotCode(String depotCode);
}
