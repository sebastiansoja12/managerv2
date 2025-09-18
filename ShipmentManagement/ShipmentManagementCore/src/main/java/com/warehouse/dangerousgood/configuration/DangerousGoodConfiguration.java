package com.warehouse.dangerousgood.configuration;

import com.warehouse.dangerousgood.infrastructure.adapter.secondary.DangerousGoodReadRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.dangerousgood.domain.port.primary.DangerousGoodPort;
import com.warehouse.dangerousgood.domain.port.primary.DangerousGoodPortImpl;
import com.warehouse.dangerousgood.domain.port.secondary.DangerousGoodRepository;
import com.warehouse.dangerousgood.domain.port.secondary.ShipmentNotifierPort;
import com.warehouse.dangerousgood.domain.service.DangerousGoodService;
import com.warehouse.dangerousgood.domain.service.DangerousGoodServiceImpl;
import com.warehouse.dangerousgood.domain.service.ShipmentService;
import com.warehouse.dangerousgood.domain.service.ShipmentServiceImpl;
import com.warehouse.dangerousgood.infrastructure.adapter.secondary.DangerousGoodRepositoryImpl;
import com.warehouse.dangerousgood.infrastructure.adapter.secondary.ShipmentNotifierAdapter;
import com.warehouse.dangerousgood.infrastructure.adapter.secondary.ShipmentReadRepository;

@Configuration
public class DangerousGoodConfiguration {

    @Bean
    public DangerousGoodPort dangerousGoodPort(final DangerousGoodService dangerousGoodService,
                                               final ShipmentService shipmentService) {
        return new DangerousGoodPortImpl(dangerousGoodService, shipmentService);
    }

    @Bean
    public ShipmentService shipmentService(final ShipmentReadRepository repository) {
        return new ShipmentServiceImpl(repository);
    }

    @Bean
    public DangerousGoodService dangerousGoodService(final DangerousGoodRepository repository,
                                                     final ShipmentNotifierPort shipmentNotifierPort) {
        return new DangerousGoodServiceImpl(repository, shipmentNotifierPort);
    }

    @Bean
    public ShipmentNotifierPort shipmentNotifierPort(final ApplicationEventPublisher publisher) {
        return new ShipmentNotifierAdapter(publisher);
    }

    @Bean
    public DangerousGoodRepository dangerousGoodRepository(final DangerousGoodReadRepository repository) {
        return new DangerousGoodRepositoryImpl(repository);
    }
}
