package com.warehouse.organisationstructure.department.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.warehouse.organisationstructure.department"})
@EntityScan(basePackages = { "com.warehouse.organisationstructure.department" })
@EnableJpaRepositories(basePackages = {"com.warehouse.organisationstructure.department"})
public class DepotTestConfiguration {
}
