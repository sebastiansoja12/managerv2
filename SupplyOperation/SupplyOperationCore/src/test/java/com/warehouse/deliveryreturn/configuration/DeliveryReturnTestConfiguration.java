package com.warehouse.deliveryreturn.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestClient;

@ComponentScan(basePackages = { "com.warehouse.deliveryreturn" })
@EntityScan(basePackages = { "com.warehouse.deliveryreturn" })
@EnableJpaRepositories(basePackages = { "com.warehouse.deliveryreturn" })
@AutoConfigureDataJpa
public class DeliveryReturnTestConfiguration {

    @LocalServerPort
    private Integer port;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl("http://localhost:" + port).build();
    }
}
