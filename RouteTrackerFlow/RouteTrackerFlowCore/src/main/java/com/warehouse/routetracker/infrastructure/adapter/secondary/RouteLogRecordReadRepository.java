package com.warehouse.routetracker.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;

@Repository
public interface RouteLogRecordReadRepository extends JpaRepository<RouteLogRecordEntity, String> {

	@Query("SELECT r FROM RouteLogRecordEntity r LEFT JOIN r.routeLogRecordDetails d " +
            "WHERE r.parcelId = :shipmentId ORDER BY d.id DESC")
    Optional<RouteLogRecordEntity> findByShipmentId(Long shipmentId);
}
