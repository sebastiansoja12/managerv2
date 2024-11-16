package com.warehouse.depot.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.depot" })
@EntityScan(basePackages = { "com.warehouse.depot" })
@EnableJpaRepositories(basePackages = { "com.warehouse.depot" })
public class DepotTestConfiguration {
}
