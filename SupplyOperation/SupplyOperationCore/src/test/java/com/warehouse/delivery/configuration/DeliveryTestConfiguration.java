package com.warehouse.delivery.configuration;

import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.routetracker.infrastructure.api.RouteLogEventPublisher;

@ComponentScan(basePackages = { "com.warehouse.delivery", "com.warehouse.deliverytoken" })
@EntityScan(basePackages = { "com.warehouse.delivery", "com.warehouse.deliverytoken" })
@EnableJpaRepositories(basePackages = { "com.warehouse.delivery", "com.warehouse.deliverytoken" })
@AutoConfigureDataJpa
public class DeliveryTestConfiguration {

    @MockBean
    public RouteLogEventPublisher routeLogEventPublisher;

    @MockBean
    public ParcelServicePort parcelServicePort;
}
