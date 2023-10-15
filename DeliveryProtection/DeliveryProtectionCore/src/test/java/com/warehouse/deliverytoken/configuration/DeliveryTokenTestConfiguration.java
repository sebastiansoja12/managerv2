package com.warehouse.deliverytoken.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;

@ComponentScan(basePackages = { "com.warehouse.deliverytoken" })
@EntityScan(basePackages = { "com.warehouse.deliverytoken" })
@EnableJpaRepositories(basePackages = { "com.warehouse.deliverytoken"})
public class DeliveryTokenTestConfiguration {

    @MockBean
    public ParcelServicePort parcelServicePort;

}
