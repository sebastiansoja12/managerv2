package com.warehouse.csv;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.csv" })
@EntityScan(basePackages = { "com.warehouse.csv" })
@EnableJpaRepositories(basePackages = { "com.warehouse.csv" })
public class CsvTestConfiguration {
}
