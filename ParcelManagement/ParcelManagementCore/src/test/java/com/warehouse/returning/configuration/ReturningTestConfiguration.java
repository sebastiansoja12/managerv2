package com.warehouse.returning.configuration;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.returning" })
@EntityScan(basePackages = { "com.warehouse.returning" })
@EnableJpaRepositories(basePackages = { "com.warehouse.returning" })
public class ReturningTestConfiguration {

}
