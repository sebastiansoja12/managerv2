package com.warehouse.supplier.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.supplier"})
@EntityScan(basePackages = { "com.warehouse.supplier"})
@EnableJpaRepositories(basePackages = { "com.warehouse.supplier"})
public class SupplierTestConfiguration {

}
