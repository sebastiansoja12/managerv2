package com.warehouse.voronoi.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.warehouse.voronoi"})
@EntityScan(basePackages = { "com.warehouse.voronoi" })
@EnableJpaRepositories(basePackages = {"com.warehouse.voronoi"})
public class VoronoiTestConfiguration {
}
