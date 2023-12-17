package com.warehouse.returntoken.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.returntoken" })
@EntityScan(basePackages = { "com.warehouse.returntoken" })
@EnableJpaRepositories(basePackages = { "com.warehouse.returntoken"})
public class ReturnTokenTestConfiguration {
}
