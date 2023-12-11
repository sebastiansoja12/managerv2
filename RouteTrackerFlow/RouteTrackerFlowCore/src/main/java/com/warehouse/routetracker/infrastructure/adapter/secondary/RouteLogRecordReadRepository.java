package com.warehouse.routetracker.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;

@Repository
public interface RouteLogRecordReadRepository extends JpaRepository<RouteLogRecordEntity, String> {
    Optional<RouteLogRecordEntity> findByParcelId(Long parcelId);
}
