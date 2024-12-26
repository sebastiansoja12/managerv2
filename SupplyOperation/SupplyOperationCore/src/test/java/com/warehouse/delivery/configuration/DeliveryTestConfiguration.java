package com.warehouse.delivery.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.deliverymissed.DeliveryMissedService;
import com.warehouse.deliveryreject.DeliveryRejectService;
import com.warehouse.deliveryreturn.infrastructure.api.DeliveryReturnService;
import com.warehouse.routelogger.RouteLogEventPublisher;

@ComponentScan(basePackages = { "com.warehouse.delivery", "com.warehouse.deliverytoken" })
@EntityScan(basePackages = { "com.warehouse.delivery", "com.warehouse.deliverytoken" })
@EnableJpaRepositories(basePackages = { "com.warehouse.delivery", "com.warehouse.deliverytoken" })
@AutoConfigureDataJpa
public class DeliveryTestConfiguration {

    @MockBean
    private RouteLogEventPublisher routeLogEventPublisher;

    @MockBean
    DeliveryMissedService deliveryMissedService;

    @MockBean
    DeliveryRejectService deliveryRejectService;

    @MockBean
    DeliveryReturnService deliveryReturnService;
}
