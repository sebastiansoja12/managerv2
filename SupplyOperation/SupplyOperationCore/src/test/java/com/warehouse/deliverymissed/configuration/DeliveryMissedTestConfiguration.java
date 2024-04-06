package com.warehouse.deliverymissed.configuration;

import com.warehouse.routelogger.RouteLogEventPublisher;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.warehouse.deliverymissed"})
@EntityScan(basePackages = { "com.warehouse.deliverymissed"})
@EnableJpaRepositories(basePackages = { "com.warehouse.deliverymissed"})
public class DeliveryMissedTestConfiguration {

    @MockBean
    public RouteLogEventPublisher routeLogEventPublisher;
}
