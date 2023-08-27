package com.warehouse.delivery.configuration;

import com.warehouse.route.infrastructure.api.RouteLogEventPublisher;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.delivery" })
@EntityScan(basePackages = { "com.warehouse.delivery" })
@EnableJpaRepositories(basePackages = { "com.warehouse.delivery" })
public class DeliveryTestConfiguration {

    @MockBean
    public RouteLogEventPublisher routeLogEventPublisher;
}
