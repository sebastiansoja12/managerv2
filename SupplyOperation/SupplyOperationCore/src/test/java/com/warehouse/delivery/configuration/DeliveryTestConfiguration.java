package com.warehouse.delivery.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.delivery.domain.port.secondary.SupplierTokenServicePort;
import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;

@ComponentScan(basePackages = { "com.warehouse.delivery" })
@EntityScan(basePackages = { "com.warehouse.delivery" })
@EnableJpaRepositories(basePackages = { "com.warehouse.delivery" })
@AutoConfigureDataJpa
public class DeliveryTestConfiguration {

    @MockBean
    public RouteLogEventPublisher routeLogEventPublisher;

    @MockBean
    public SupplierTokenServicePort supplierTokenServicePort;
}
