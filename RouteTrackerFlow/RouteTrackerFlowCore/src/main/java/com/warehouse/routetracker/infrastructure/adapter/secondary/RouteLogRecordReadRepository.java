package com.warehouse.routetracker.infrastructure.adapter.secondary;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteLogRecordReadRepository extends JpaRepository<RouteLogRecordEntity, UUID> {
    Optional<RouteLogRecordEntity> findByParcelId(Long parcelId);
}
