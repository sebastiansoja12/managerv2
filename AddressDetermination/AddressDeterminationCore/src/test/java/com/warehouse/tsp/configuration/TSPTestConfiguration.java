package com.warehouse.tsp.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.tsp" })
@EntityScan(basePackages = { "com.warehouse.tsp" })
@EnableJpaRepositories(basePackages = { "com.warehouse.tsp" })
public class TSPTestConfiguration {
}
