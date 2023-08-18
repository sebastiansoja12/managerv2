package com.warehouse.redirect.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.redirect", "com.warehouse.mail" })
@EntityScan(basePackages = { "com.warehouse.redirect", "com.warehouse.mail" })
@EnableJpaRepositories(basePackages = { "com.warehouse.redirect", "com.warehouse.mail" })
public class RedirectTokenTestConfiguration {
}
