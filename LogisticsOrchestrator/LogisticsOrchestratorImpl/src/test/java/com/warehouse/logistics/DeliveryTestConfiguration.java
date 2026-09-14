package com.warehouse.logistics;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.data.jpa.test.autoconfigure.AutoConfigureDataJpa;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.warehouse.deliverymissed.DeliveryMissedService;
import com.warehouse.deliveryreject.DeliveryRejectService;
import com.warehouse.deliveryreturn.infrastructure.api.DeliveryReturnService;

@ComponentScan(basePackages = { "com.warehouse.delivery", "com.warehouse.deliverytoken" })
@EntityScan(basePackages = { "com.warehouse.delivery", "com.warehouse.deliverytoken" })
@EnableJpaRepositories(basePackages = { "com.warehouse.delivery", "com.warehouse.deliverytoken" })
@AutoConfigureDataJpa
public class DeliveryTestConfiguration {

    @MockitoBean
    DeliveryMissedService deliveryMissedService;

    @MockitoBean
    DeliveryRejectService deliveryRejectService;

    @MockitoBean
    DeliveryReturnService deliveryReturnService;
}
