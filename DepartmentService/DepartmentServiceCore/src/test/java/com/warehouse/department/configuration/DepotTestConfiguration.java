package com.warehouse.department.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.warehouse.department"})
@EntityScan(basePackages = { "com.warehouse.depot" })
@EnableJpaRepositories(basePackages = {"com.warehouse.department"})
public class DepotTestConfiguration {
}
