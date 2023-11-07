package com.warehouse.deliveryreturn.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestClient;

@ComponentScan(basePackages = { "com.warehouse.deliveryreturn", "com.warehouse.parcelstatuschange" })
@EntityScan(basePackages = { "com.warehouse.deliveryreturn", "com.warehouse.parcelstatuschange" })
@EnableJpaRepositories(basePackages = { "com.warehouse.deliveryreturn", "com.warehouse.parcelstatuschange" })
@AutoConfigureDataJpa
@EnableAutoConfiguration
public class DeliveryReturnTestConfiguration {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl("http://localhost:8080").build();
    }
}
