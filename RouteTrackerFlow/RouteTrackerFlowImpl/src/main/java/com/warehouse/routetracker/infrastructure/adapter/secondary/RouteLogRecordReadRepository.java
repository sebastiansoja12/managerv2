package com.warehouse.routetracker.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteLogRecordEntity;

@Repository
public interface RouteLogRecordReadRepository extends JpaRepository<RouteLogRecordEntity, String> {

    @EntityGraph(attributePaths = "routeLogRecordDetails")
    @Query("SELECT r FROM RouteLogRecordEntity r WHERE r.shipmentId = :shipmentId")
    Optional<RouteLogRecordEntity> findByShipmentId(ShipmentId shipmentId);

    @Override
    @EntityGraph(attributePaths = "routeLogRecordDetails")
    List<RouteLogRecordEntity> findAll();
}
