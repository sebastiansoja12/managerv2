package com.warehouse.parcelstatuschange;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.parcelstatuschange" })
@EntityScan(basePackages = { "com.warehouse.parcelstatuschange" })
@EnableJpaRepositories(basePackages = { "com.warehouse.parcelstatuschange" })
public class ParcelStatusChangeConfigurationTest {
}
