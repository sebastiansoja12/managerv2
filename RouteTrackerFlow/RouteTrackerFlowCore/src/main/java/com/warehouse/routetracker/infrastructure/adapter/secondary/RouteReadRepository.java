package com.warehouse.routetracker.infrastructure.adapter.secondary;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteEntity;

@Repository
public interface RouteReadRepository extends JpaRepository<RouteEntity, String> {

    @EntityGraph(value = "RouteEntity.full", type = EntityGraph.EntityGraphType.FETCH)
    List<RouteEntity> findByParcelId(Long parcelId);

    @EntityGraph(value = "RouteEntity.full", type = EntityGraph.EntityGraphType.FETCH)
    List<RouteEntity> findAllByUserUsername(String username);

}
