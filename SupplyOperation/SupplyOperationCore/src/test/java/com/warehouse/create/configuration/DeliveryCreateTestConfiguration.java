package com.warehouse.create.configuration;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.create" })
@EntityScan(basePackages = { "com.warehouse.create" })
@EnableJpaRepositories(basePackages = { "com.warehouse.create" })
@AutoConfigureDataJpa
public class DeliveryCreateTestConfiguration {
}
