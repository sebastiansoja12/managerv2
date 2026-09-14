package com.warehouse.deliverytoken.configuration;

import org.mockito.Mockito;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;

@ComponentScan(basePackages = { "com.warehouse.deliverytoken" })
@EntityScan(basePackages = { "com.warehouse.deliverytoken" })
@EnableJpaRepositories(basePackages = { "com.warehouse.deliverytoken"})
public class DeliveryTokenTestConfiguration {

    @Bean
    public ParcelServicePort parcelServicePort() {
        return Mockito.mock(ParcelServicePort.class);
    }

}
