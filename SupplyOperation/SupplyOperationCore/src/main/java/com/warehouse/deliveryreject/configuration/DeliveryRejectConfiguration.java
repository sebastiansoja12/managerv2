package com.warehouse.deliveryreject.configuration;

import com.warehouse.deliveryreject.DeliveryRejectService;
import com.warehouse.deliveryreject.domain.port.primary.DeliveryRejectPort;
import com.warehouse.deliveryreject.domain.port.primary.DeliveryRejectPortImpl;
import com.warehouse.deliveryreject.infrastructure.adapter.primary.DeliveryRejectAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeliveryRejectConfiguration {

    @Bean
    public DeliveryRejectPort deliveryRejectPort() {
        return new DeliveryRejectPortImpl();
    }

    @Bean
    public DeliveryRejectService deliveryRejectService(final DeliveryRejectPort deliveryRejectPort) {
        return new DeliveryRejectAdapter(deliveryRejectPort);
    }
}
