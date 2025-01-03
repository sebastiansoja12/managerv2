package com.warehouse.deliveryreject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.deliveryreject.DeliveryRejectService;
import com.warehouse.deliveryreject.domain.port.primary.DeliveryRejectPort;
import com.warehouse.deliveryreject.domain.port.primary.DeliveryRejectPortImpl;
import com.warehouse.deliveryreject.domain.port.secondary.RejectRepository;
import com.warehouse.deliveryreject.domain.service.RejectService;
import com.warehouse.deliveryreject.infrastructure.adapter.primary.DeliveryRejectAdapter;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.RejectRepositoryImpl;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.RejectReasonReadRepository;

@Configuration
public class DeliveryRejectConfiguration {

    @Bean
    public DeliveryRejectPort deliveryRejectPort(final RejectService rejectService) {
        return new DeliveryRejectPortImpl(rejectService);
    }

    @Bean
    public DeliveryRejectService deliveryRejectService(final DeliveryRejectPort deliveryRejectPort) {
        return new DeliveryRejectAdapter(deliveryRejectPort);
    }

    @Bean
    public RejectRepository deliveryRejectRepository(final RejectReasonReadRepository rejectReasonReadRepository) {
        return new RejectRepositoryImpl(rejectReasonReadRepository);
    }
}
