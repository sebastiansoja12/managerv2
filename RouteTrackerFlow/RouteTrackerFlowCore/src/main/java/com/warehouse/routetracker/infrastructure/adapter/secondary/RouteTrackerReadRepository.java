package com.warehouse.routetracker.infrastructure.adapter.secondary;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteEntity;


@Repository
public interface RouteTrackerReadRepository extends MongoRepository<RouteEntity, String> {

}
