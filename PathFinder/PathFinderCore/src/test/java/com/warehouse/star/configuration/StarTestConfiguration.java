package com.warehouse.star.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.star" })
@EntityScan(basePackages = { "com.warehouse.star" })
@EnableJpaRepositories(basePackages = { "com.warehouse.star" })
public class StarTestConfiguration {
}
