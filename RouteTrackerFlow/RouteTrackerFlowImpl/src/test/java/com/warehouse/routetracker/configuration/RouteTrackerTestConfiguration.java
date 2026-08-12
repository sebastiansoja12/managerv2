package com.warehouse.routetracker.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan(basePackages = { "com.warehouse.routetracker", "com.warehouse.depot"})
@EnableJpaRepositories(basePackages = { "com.warehouse.routetracker", "com.warehouse.depot"})
public class RouteTrackerTestConfiguration {
}
